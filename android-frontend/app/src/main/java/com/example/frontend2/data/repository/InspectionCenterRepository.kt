package com.example.frontend2.data.repository

import com.example.frontend2.data.network.datasource.InspectionCenterDataSource
import com.example.frontend2.data.network.dto.response.InspectionCenterByIdResponse
import com.example.frontend2.data.network.dto.response.InspectionCenterResponse
import com.example.frontend2.util.Resource
import javax.inject.Inject

class InspectionCenterRepository @Inject constructor(
    private val inspectionCenterDataSource: InspectionCenterDataSource
) {

    // Lấy danh sách trung tâm đăng kiểm
    suspend fun getInspectionCenters(
        provinceId: String,
        districtId: String,
        wardId: String
    ): Resource<List<InspectionCenterResponse>> {
        val resultResponse =
            inspectionCenterDataSource.getInspectionCenters(provinceId, districtId, wardId)

        return when (resultResponse.success) {
            true -> {
                Resource.Success(resultResponse.data ?: emptyList())
            }

            false -> {
                Resource.Error(
                    exception = Exception(
                        resultResponse.message
                            ?: "Không thể tải danh sách trung tâm đăng kiểm gần bạn"
                    ),
                    errorData = resultResponse.errors
                )
            }
        }
    }

    // Chi tiết trung tâm đăng kiểm theo mã trung tâm
    suspend fun getInspectionCenterById(
        inspectionCenterId: String
    ): Resource<InspectionCenterByIdResponse> {
        val resultResponse = inspectionCenterDataSource.getInspectionCenterById(inspectionCenterId)

        return when (resultResponse.success) {
            true -> {
                Resource.Success(
                    resultResponse.data
                        ?: throw Exception("Không tìm thấy trung tâm đăng kiểm trên server")
                )
            }

            false -> {
                Resource.Error(
                    exception = Exception(
                        resultResponse.message ?: "Không thể tải thông tin trung tâm đăng kiểm"
                    ),
                    errorData = resultResponse.errors
                )
            }
        }
    }

}