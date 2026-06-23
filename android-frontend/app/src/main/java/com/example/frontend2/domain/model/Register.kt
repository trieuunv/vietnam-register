package com.example.frontend2.domain.model

data class Register(
    val userName: String,
    val email: String,
    val password: String,
    val confirmPassword: String,
    val fullName: String,
    val phone: String,
)

data class RegisterErrorState(
    val userNameError: String = "",
    val emailError: String = "",
    val passwordError: String = "",
    val confirmPasswordError: String = "",
    val fullNameError: String = "",
    val phoneError: String = ""
)