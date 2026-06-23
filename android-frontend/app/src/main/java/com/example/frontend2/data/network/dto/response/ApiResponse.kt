package com.example.frontend2.data.network.dto.response

data class ApiResponse<T>(
    val success: Boolean,
    val statusCode: Int?,
    val data: T?,
    val message: String?,
    val errors: Map<String, List<String>>? = null
)