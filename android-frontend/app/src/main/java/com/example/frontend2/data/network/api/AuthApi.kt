package com.example.frontend2.data.network.api

import com.example.frontend2.data.network.dto.request.LoginRequest
import com.example.frontend2.data.network.dto.request.RefreshTokenRequest
import com.example.frontend2.data.network.dto.request.RegisterRequest
import com.example.frontend2.data.network.dto.response.ApiResponse
import com.example.frontend2.data.network.dto.response.AuthResponse
import com.example.frontend2.data.network.dto.response.RefreshTokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    /**
     * Authenticate a user with their login credentials.
     *
     * @param loginRequest The request body containing the user's login credentials.
     * @return A [Response] containing the [AuthResponse] with the authentication result.
     */
    @POST("client/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<ApiResponse<AuthResponse>>

    /**
     * Register a new user with their registration details.
     *
     * @param registerRequest The request body containing the user's registration details.
     * @return A [Response] containing the [AuthResponse] with the registration result.
     */
    @POST("client/auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<ApiResponse<AuthResponse>>

    /**
     * Refresh the user's access token using the refresh token.
     *
     * @param refreshAccessToken The request body containing the refresh token.
     * @return A [Response] containing the [RefreshTokenResponse] with the new access token.
     */
    @POST("client/auth/refresh")
    suspend fun refreshToken(@Body refreshAccessToken: RefreshTokenRequest): Response<ApiResponse<RefreshTokenResponse>>

}