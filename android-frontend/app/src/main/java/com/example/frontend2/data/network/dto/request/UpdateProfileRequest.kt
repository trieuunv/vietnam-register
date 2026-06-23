package com.example.frontend2.data.network.dto.request

import com.google.gson.annotations.SerializedName

data class UpdateProfileRequest(
    @SerializedName("full_name")
    val fullName: String,

    @SerializedName("day_of_birth")
    val dayOfBirth: String,

    @SerializedName("gender")
    val gender: String,
)