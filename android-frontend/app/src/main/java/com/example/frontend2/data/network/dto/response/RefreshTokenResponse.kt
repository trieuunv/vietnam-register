package com.example.frontend2.data.network.dto.response

data class RefreshTokenResponse(
    val accessToken: String,
    val expiresIn: String
)