package com.example.frontend2.data.network.api

import com.example.frontend2.data.network.dto.request.UpdateProfileRequest
import com.example.frontend2.data.network.dto.response.ApiResponse
import com.example.frontend2.data.network.dto.response.UserResponse
import com.example.frontend2.domain.model.Profile
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface UserApi {

    /**
     * Get the current user's profile.
     *
     * @return A [Response] containing an [ApiResponse] with the user's profile data.
     */
    @GET("user")
    suspend fun getMe(): Response<ApiResponse<UserResponse>>

    /**
     * Update the current user's profile.
     *
     * @param updateProfileRequest The updated profile data.
     * @return A [Response] containing an [ApiResponse] with the updated profile data.
     */
    @PUT("client/owner")
    suspend fun updateProfile(@Body updateProfileRequest: UpdateProfileRequest): Response<ApiResponse<Profile>>

}