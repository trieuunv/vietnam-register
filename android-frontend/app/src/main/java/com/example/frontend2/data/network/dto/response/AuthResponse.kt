package com.example.frontend2.data.network.dto.response

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
    val expiresIn: String
)
