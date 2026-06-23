package com.example.frontend2.data.network.api

import com.example.frontend2.data.network.dto.response.ApiResponse
import com.example.frontend2.data.network.dto.response.InspectionCenterByIdResponse
import com.example.frontend2.data.network.dto.response.InspectionCenterResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface InspectionCenterApi {

    /**
     * Get a list of inspection centers by province, district, and ward codes.
     *
     * @param provinceId The province code.
     * @param districtId The district code.
     * @param wardId The ward code.
     * @return A [Response] containing the [ApiResponse] with a list of [InspectionCenterResponse].
     */
    @GET("client/centers/by-address")
    suspend fun getInspectionCenters(
        @Query("province_code") provinceId: String,
        @Query("district_code") districtId: String,
        @Query("ward_code") wardId: String
    ): Response<ApiResponse<List<InspectionCenterResponse>>>

    /**
     * Get an inspection center by its ID.
     *
     * @param inspectionCenterId The ID of the inspection center.
     * @return A [Response] containing the [ApiResponse] with the [InspectionCenterByIdResponse].
     */
    @GET("client/centers/{inspectionCenterId}")
    suspend fun getInspectionCenterById(@Path("inspectionCenterId") inspectionCenterId: String): Response<ApiResponse<InspectionCenterByIdResponse>>

}