package com.example.frontend2.data.network.datasource

import android.util.Log
import com.example.frontend2.data.network.api.AuthApi
import com.example.frontend2.data.network.dto.request.LoginRequest
import com.example.frontend2.data.network.dto.request.RefreshTokenRequest
import com.example.frontend2.data.network.dto.request.RegisterRequest
import com.example.frontend2.data.network.dto.response.ApiResponse
import com.example.frontend2.data.network.dto.response.AuthResponse
import com.example.frontend2.data.network.dto.response.RefreshTokenResponse
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class AuthDataSource @Inject constructor(
    @Named("no_auth") private val retrofit: Retrofit
) : BaseDataSource() {

    // Khởi tạo Retrofit API cho AuthApi
    private val authApi: AuthApi by lazy {
        retrofit.create(AuthApi::class.java)
    }

    // Gọi API để lấy refresh token
    suspend fun refreshToken(refreshToken: String): ApiResponse<RefreshTokenResponse> {
        val refreshTokenRequest = RefreshTokenRequest(refreshToken)
        return safeApiCall { authApi.refreshToken(refreshTokenRequest) }
    }

    // Gọi API để đăng nhập
    suspend fun login(email: String, password: String): ApiResponse<AuthResponse> {
        val loginRequest = LoginRequest(email, password)
        return safeApiCall {
            val result = authApi.login(loginRequest)
            Log.d("exceptionAuthDataSource", "login: $result")
            result
        }
    }

    // Gọi API để đăng ký
    suspend fun register(
        userName: String,
        email: String,
        password: String,
        fullName: String,
        phone: String
    ): ApiResponse<AuthResponse> {
        val registerRequest = RegisterRequest(userName, email, password, fullName, phone)
        return safeApiCall { authApi.register(registerRequest) }
    }

}