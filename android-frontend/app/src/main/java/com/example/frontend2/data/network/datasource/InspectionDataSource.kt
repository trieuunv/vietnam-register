package com.example.frontend2.data.network.datasource

import com.example.frontend2.data.network.api.InspectionApi
import com.example.frontend2.data.network.dto.response.ApiResponse
import com.example.frontend2.data.network.dto.response.InspectionByIdResponse
import com.example.frontend2.data.network.dto.response.InspectionsListResponse
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named

class InspectionDataSource @Inject constructor(
    @Named("auth") private val retrofit: Retrofit
) : BaseDataSource() {

    private val inspectionApi: InspectionApi by lazy {
        retrofit.create(InspectionApi::class.java)
    }

    /**
     * Retrieves a list of inspections.
     *
     * @return A [Response] containing the [ApiResponse] with the list of inspections.
     */
    suspend fun getAllInspections(): ApiResponse<List<InspectionsListResponse>> {
        return safeApiCall { inspectionApi.getAllInspections() }
    }

    /**
     * Retrieves an inspection by its ID.
     *
     * @param inspectionId The ID of the inspection to retrieve.
     * @return A [Response] containing the [ApiResponse] with the inspection details.
     */
    suspend fun getInspectionById(inspectionId: String): ApiResponse<InspectionByIdResponse> {
        return safeApiCall { inspectionApi.getInspectionById(inspectionId) }
    }

}