package com.example.frontend2.data.repository

import com.example.frontend2.data.network.datasource.InspectionDataSource
import com.example.frontend2.data.network.dto.response.InspectionByIdResponse
import com.example.frontend2.data.network.dto.response.InspectionsListResponse
import com.example.frontend2.util.Resource
import javax.inject.Inject

class InspectionRepository @Inject constructor(
    private val inspectionDataSource: InspectionDataSource
) {

    suspend fun getAllInspections(): Resource<List<InspectionsListResponse>> {
        val inspectionResponse = inspectionDataSource.getAllInspections()

        return when (inspectionResponse.success) {
            true -> {
                Resource.Success(inspectionResponse.data ?: emptyList())
            }

            false -> {
                Resource.Error(
                    exception = Exception(
                        inspectionResponse.message ?: "Không thể tải danh sách đăng kiểm"
                    ),
                    errorData = inspectionResponse.errors
                )
            }
        }
    }

    suspend fun getInspectionById(inspectionId: String): Resource<InspectionByIdResponse> {
        val inspectionResponse = inspectionDataSource.getInspectionById(inspectionId)

        return when (inspectionResponse.success) {
            true -> {
                Resource.Success(
                    inspectionResponse.data ?: throw Exception("Không tìm thấy thông tin đăng kiểm")
                )
            }

            false -> {
                Resource.Error(
                    exception = Exception(
                        inspectionResponse.message ?: "Không thể tải thông tin đăng kiểm"
                    ),
                    errorData = inspectionResponse.errors
                )
            }
        }
    }

}