package com.example.frontend2.data.network.datasource

import com.example.frontend2.data.network.api.InspectionCenterApi
import com.example.frontend2.data.network.dto.response.ApiResponse
import com.example.frontend2.data.network.dto.response.InspectionCenterByIdResponse
import com.example.frontend2.data.network.dto.response.InspectionCenterResponse
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class InspectionCenterDataSource @Inject constructor(
    @Named("auth") private val retrofit: Retrofit
) : BaseDataSource() {

    private val inspectionCenterApi: InspectionCenterApi by lazy {
        retrofit.create(InspectionCenterApi::class.java)
    }

    /**
     * Fetches a list of inspection centers based on the provided province, district, and ward IDs.
     *
     * @param provinceId The ID of the province.
     * @param districtId The ID of the district.
     * @param wardId The ID of the ward.
     * @return An [ApiResponse] containing a list of [InspectionCenterResponse] objects.
     */
    suspend fun getInspectionCenters(
        provinceId: String,
        districtId: String,
        wardId: String
    ): ApiResponse<List<InspectionCenterResponse>> {
        return safeApiCall {
            inspectionCenterApi.getInspectionCenters(
                provinceId,
                districtId,
                wardId
            )
        }
    }

    /**
     * Fetches the details of an inspection center by its ID.
     *
     * @param inspectionCenterId The ID of the inspection center.
     * @return An [ApiResponse] containing the details of the inspection center as an [InspectionCenterByIdResponse].
     */
    suspend fun getInspectionCenterById(inspectionCenterId: String): ApiResponse<InspectionCenterByIdResponse> {
        return safeApiCall { inspectionCenterApi.getInspectionCenterById(inspectionCenterId) }
    }

}