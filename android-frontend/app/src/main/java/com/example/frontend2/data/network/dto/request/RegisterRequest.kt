package com.example.frontend2.data.network.dto.request

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("username")
    val userName: String,

    val email: String,
    val password: String,

    @SerializedName("full_name")
    val fullName: String,

    val phone: String
)