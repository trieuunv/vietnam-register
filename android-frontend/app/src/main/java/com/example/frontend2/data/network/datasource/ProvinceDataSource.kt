package com.example.frontend2.data.network.datasource

import com.example.frontend2.data.network.api.ProvinceApi
import com.example.frontend2.data.network.dto.response.ApiResponse
import com.example.frontend2.data.network.dto.response.DistrictResponse
import com.example.frontend2.data.network.dto.response.ProvinceResponse
import com.example.frontend2.data.network.dto.response.WardResponse
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class ProvinceDataSource @Inject constructor(
    @Named("no_auth") private val retrofit: Retrofit
) : BaseDataSource() {

    private val provinceApi: ProvinceApi by lazy {
        retrofit.create(ProvinceApi::class.java)
    }

    /**
     * Fetches the list of provinces from the API.
     *
     * @return [ApiResponse] containing a list of [ProvinceResponse] objects.
     */
    suspend fun getProvinces(): ApiResponse<List<ProvinceResponse>> {
        return safeApiCall { provinceApi.getProvinces() }
    }

    /**
     * Fetches the list of districts for a given province from the API.
     *
     * @param provinceId The ID of the province for which to fetch districts.
     * @return [ApiResponse] containing a list of [DistrictResponse] objects.
     */
    suspend fun getDistricts(provinceId: String): ApiResponse<List<DistrictResponse>> {
        return safeApiCall { provinceApi.getDistricts(provinceId) }
    }

    /**
     * Fetches the list of wards for a given district from the API.
     *
     * @param districtId The ID of the district for which to fetch wards.
     * @return [ApiResponse] containing a list of [WardResponse] objects.
     */
    suspend fun getWards(districtId: String): ApiResponse<List<WardResponse>> {
        return safeApiCall { provinceApi.getWards(districtId) }
    }

}