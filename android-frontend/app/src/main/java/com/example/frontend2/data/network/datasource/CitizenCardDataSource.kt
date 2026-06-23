package com.example.frontend2.data.network.datasource

import com.example.frontend2.data.network.api.CitizenCardApi
import com.example.frontend2.data.network.dto.request.CitizenCardRequest
import com.example.frontend2.data.network.dto.response.ApiResponse
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class CitizenCardDataSource @Inject constructor(
    @Named("auth") private val retrofit: Retrofit
) : BaseDataSource() {

    private val citizenCardApi: CitizenCardApi by lazy {
        retrofit.create(CitizenCardApi::class.java)
    }

    /**
     * This method is used to make a network call to the citizen card API.
     * It takes a [CitizenCardRequest] object as a parameter and returns an [ApiResponse] object.
     *
     * @param citizenCardRequest The request object containing the necessary data for the API call.
     * @return An [ApiResponse] object containing the [citizenCardRequest] from the API.
     */
    suspend fun citizenCard(citizenCardRequest: CitizenCardRequest): ApiResponse<CitizenCardRequest> {
        return safeApiCall { citizenCardApi.citizenCard(citizenCardRequest) }
    }

}
