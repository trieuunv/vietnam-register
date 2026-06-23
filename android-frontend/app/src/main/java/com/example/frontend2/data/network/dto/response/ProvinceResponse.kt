package com.example.frontend2.data.network.dto.response

data class ProvinceResponse(
    val name: String,
    val slug: String,
    val type: String,
    val name_with_type: String,
    val code: String
)

data class DistrictResponse(
    val name: String,
    val type: String,
    val slug: String,
    val name_with_type: String,
    val path: String,
    val path_with_type: String,
    val code: String,
    val parent_code: String
)

data class WardResponse(
    val name: String,
    val type: String,
    val slug: String,
    val name_with_type: String,
    val path: String,
    val path_with_type: String,
    val code: String,
    val parent_code: String
)