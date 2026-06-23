package com.example.frontend2.data.repository

import com.example.frontend2.data.network.datasource.ImgurDataSource
import com.example.frontend2.data.network.dto.response.UploadResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class ImgurRepository @Inject constructor(
    private val imgurDataSource: ImgurDataSource
) {
    suspend fun uploadImage(
        imageFile: File,
        title: String? = null
    ): Result<UploadResponse> = withContext(Dispatchers.IO) {
        try {
            val response = imgurDataSource.uploadImage(imageFile, title)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Tải ảnh lên thất bại: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}