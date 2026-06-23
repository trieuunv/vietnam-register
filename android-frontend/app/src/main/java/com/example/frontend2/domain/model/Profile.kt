package com.example.frontend2.domain.model

data class Profile(
    val id: String,
    val fullName: String,
    val dayOfBirth: String? = null,
    val gender: String? = null,
    val phone: String,
    val email: String,
    val isVerified: Boolean,
    val createdAt: String,
)