package com.example.frontend2.data.network.dto.response

import com.google.gson.annotations.SerializedName

class UserResponse(
    @SerializedName("id") val id: String,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("dayOfBirth") val dayOfBirth: String?,
    @SerializedName("gender") val gender: String?,
    @SerializedName("phone") val phone: String,
    @SerializedName("email") val email: String,
    @SerializedName("isVerified") val isVerified: Boolean,
    @SerializedName("createdAt") val createdAt: String,
)