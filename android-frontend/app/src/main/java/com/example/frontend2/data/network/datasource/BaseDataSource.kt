package com.example.frontend2.data.network.datasource

import com.example.frontend2.data.network.dto.response.ApiResponse
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import retrofit2.HttpException
import retrofit2.Response

open class BaseDataSource {
    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<ApiResponse<T>>): ApiResponse<T> {
        return try {
            val response = apiCall()

            if (response.isSuccessful) {
                val responseBody = response.body()

                if (responseBody != null) {
                    ApiResponse(
                        success = responseBody.success,
                        statusCode = response.code(),
                        data = responseBody.data,
                        message = responseBody.message,
                        errors = null
                    )
                } else {
                    ApiResponse(
                        success = false,
                        statusCode = response.code(),
                        data = null,
                        message = "Response body is null",
                        errors = null
                    )
                }
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = parseErrorResponse(errorBody)

                ApiResponse(
                    success = false,
                    statusCode = response.code(),
                    data = null,
                    message = errorResponse?.message ?: "Lỗi không xác định",
                    errors = errorResponse?.errors
                )
            }
        } catch (e: HttpException) {
            ApiResponse(
                success = false,
                statusCode = e.code(),
                data = null,
                message = "Http error: ${e.code()}",
                errors = null
            )
        } catch (e: Exception) {
            ApiResponse(
                success = false,
                statusCode = null,
                data = null,
                message = "Unknown error: " + e.message,
                errors = null
            )
        }
    }

    private fun parseErrorResponse(errorBody: String?): ApiResponse<Unit>? {
        return try {
            errorBody?.let {
                val type = object : TypeToken<ApiResponse<Unit>>() {}.type
                Gson().fromJson(it, type)
            }
        } catch (e: JsonSyntaxException) {
            null
        }
    }
}