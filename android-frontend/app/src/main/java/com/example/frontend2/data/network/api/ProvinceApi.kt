package com.example.frontend2.data.network.api

import com.example.frontend2.data.network.dto.response.ApiResponse
import com.example.frontend2.data.network.dto.response.DistrictResponse
import com.example.frontend2.data.network.dto.response.ProvinceResponse
import com.example.frontend2.data.network.dto.response.WardResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProvinceApi {

    /**
     * Fetches a list of provinces.
     *
     * @return A [Response] containing an [ApiResponse] with a list of [ProvinceResponse].
     */
    @GET("provinces")
    suspend fun getProvinces(): Response<ApiResponse<List<ProvinceResponse>>>

    /**
     * Fetches a list of districts for a given province.
     *
     * @param provinceId The ID of the province.
     * @return A [Response] containing an [ApiResponse] with a list of [DistrictResponse].
     */
    @GET("provinces/get-districts/{provinceId}")
    suspend fun getDistricts(@Path("provinceId") provinceId: String): Response<ApiResponse<List<DistrictResponse>>>

    /**
     * Fetches a list of wards for a given district.
     *
     * @param districtId The ID of the district.
     * @return A [Response] containing an [ApiResponse] with a list of [WardResponse].
     */
    @GET("provinces/get-wards/{districtId}")
    suspend fun getWards(@Path("districtId") districtId: String): Response<ApiResponse<List<WardResponse>>>

}