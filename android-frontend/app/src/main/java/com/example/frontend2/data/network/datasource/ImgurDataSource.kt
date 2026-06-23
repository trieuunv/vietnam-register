package com.example.frontend2.data.network.datasource

import com.example.frontend2.data.network.api.ApiKeys
import com.example.frontend2.data.network.api.ImgurApi
import com.example.frontend2.data.network.dto.response.UploadResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Inject

class ImgurDataSource @Inject constructor() {

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("Authorization", "Client-ID ${ApiKeys.CLIENT_ID}")
                .method(original.method, original.body)
                .build()
            chain.proceed(request)
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.imgur.com")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    private val imgurApi = retrofit.create(ImgurApi::class.java)

    suspend fun uploadImage(
        imageFile: File,
        title: String? = null
    ): Response<UploadResponse> {
        val requestFile = imageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)

        val namePart = title?.toRequestBody("text/plain".toMediaTypeOrNull())

        return imgurApi.uploadFile(imagePart, namePart)
    }

}