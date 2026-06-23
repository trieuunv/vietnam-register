package com.example.frontend2.domain.model

data class AccessToken(
    val token: String,
    val expireAt: Long
)