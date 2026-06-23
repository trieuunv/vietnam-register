package com.example.frontend2.data.network.dto.response

data class InspectionCenterResponse(
    val id: String,
    val name: String,
    val code: String,
    val status: String,
    val fullAddress: String
)

data class InspectionCenterByIdResponse(
    val id: Int,
    val name: String,
    val code: String,
    val phone: String,
    val email: String,
    val directorName: String,
    val status: String
)