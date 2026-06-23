package com.example.frontend2.data.network.api

import com.example.frontend2.data.network.dto.response.ApiResponse
import com.example.frontend2.data.network.dto.response.InspectionByIdResponse
import com.example.frontend2.data.network.dto.response.InspectionsListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface InspectionApi {

    /**
     * Retrieves a list of inspections
     *
     * @return A [Response] containing the [ApiResponse] with the list of inspections
     */
    @GET("client/inspections")
    suspend fun getAllInspections(): Response<ApiResponse<List<InspectionsListResponse>>>

    /**
     * Retrieves an inspection by its ID
     *
     * @param inspectionId The ID of the inspection to retrieve
     * @return A [Response] containing the [ApiResponse] with the inspection details
     */
    @GET("client/inspections/{inspectionId}")
    suspend fun getInspectionById(@Path("inspectionId") inspectionId: String): Response<ApiResponse<InspectionByIdResponse>>

}