package com.example.frontend2.data.network.api

import com.example.frontend2.data.network.dto.request.CitizenCardRequest
import com.example.frontend2.data.network.dto.response.ApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CitizenCardApi {

    /**
     * Authenticate a citizen card.
     *
     * @param citizenCardRequest The request body containing the citizen card information.
     * @return A [Response] containing the [ApiResponse] with the result of [CitizenCardRequest].
     */
    @POST("client/citizen-card/authenticate")
    suspend fun citizenCard(@Body citizenCardRequest: CitizenCardRequest): Response<ApiResponse<CitizenCardRequest>>

}