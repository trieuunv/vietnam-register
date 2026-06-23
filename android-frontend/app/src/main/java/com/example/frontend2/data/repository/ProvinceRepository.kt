package com.example.frontend2.data.repository

import com.example.frontend2.data.network.datasource.ProvinceDataSource
import com.example.frontend2.data.network.dto.response.DistrictResponse
import com.example.frontend2.data.network.dto.response.ProvinceResponse
import com.example.frontend2.data.network.dto.response.WardResponse
import com.example.frontend2.util.Resource
import javax.inject.Inject

class ProvinceRepository @Inject constructor(
    private val provinceDataSource: ProvinceDataSource
) {
    suspend fun getProvinces(): Resource<List<ProvinceResponse>> {
        val provincesResponse = provinceDataSource.getProvinces()

        return when (provincesResponse.success) {
            true -> {
                Resource.Success(provincesResponse.data ?: emptyList())
            }

            false -> {
                Resource.Error(
                    exception = Exception(
                        provincesResponse.message ?: "Không thể tải danh sách tỉnh thành"
                    ),
                    errorData = provincesResponse.errors
                )
            }
        }
    }

    suspend fun getDistricts(provinceId: String): Resource<List<DistrictResponse>> {
        val districtsResponse = provinceDataSource.getDistricts(provinceId)

        return when (districtsResponse.success) {
            true -> {
                Resource.Success(districtsResponse.data ?: emptyList())
            }

            false -> {
                Resource.Error(
                    exception = Exception(
                        districtsResponse.message ?: "Không thể tải danh sách quận huyện"
                    ),
                    errorData = districtsResponse.errors
                )
            }
        }
    }

    suspend fun getWards(districtId: String): Resource<List<WardResponse>> {
        val wardsResponse = provinceDataSource.getWards(districtId)

        return when (wardsResponse.success) {
            true -> {
                Resource.Success(wardsResponse.data ?: emptyList())
            }

            false -> {
                Resource.Error(
                    exception = Exception(
                        wardsResponse.message ?: "Không thể tải danh sách phường xã"
                    ),
                    errorData = wardsResponse.errors
                )
            }
        }
    }
}