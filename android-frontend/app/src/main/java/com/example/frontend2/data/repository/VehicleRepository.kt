package com.example.frontend2.data.repository

import android.util.Log
import com.example.frontend2.data.network.datasource.VehicleDataSource
import com.example.frontend2.data.network.dto.request.VehicleRegisterRequest
import com.example.frontend2.data.network.dto.request.VehicleUpdateRequest
import com.example.frontend2.data.network.dto.response.VehicleResponse
import com.example.frontend2.domain.model.Vehicle
import com.example.frontend2.util.Resource
import javax.inject.Inject

class VehicleRepository @Inject constructor(
    private val vehicleDataSource: VehicleDataSource
) {

    suspend fun getVehicles(): Resource<List<Vehicle>> {
        val vehiclesResponse = vehicleDataSource.getVehicles()

        return when (vehiclesResponse.success) {
            true -> {
                Resource.Success(vehiclesResponse.data ?: emptyList())
            }

            false -> {
                Resource.Error(
                    exception = Exception(
                        vehiclesResponse.message ?: "Không thể tải danh sách phương tiện"
                    ),
                    errorData = vehiclesResponse.errors
                )
            }
        }
    }

    suspend fun getVehicleType(): Resource<List<VehicleResponse>> {
        val vehicleResponse = vehicleDataSource.getVehicleType()

        return when (vehicleResponse.success) {
            true -> {
                Resource.Success(vehicleResponse.data ?: emptyList())
            }

            false -> {
                Resource.Error(
                    exception = Exception(
                        vehicleResponse.message ?: "Không thể tải danh sách loại phương tiện"
                    ),
                    errorData = vehicleResponse.errors
                )
            }
        }
    }

    suspend fun vehicleRegister(vehicleRegisterRequest: VehicleRegisterRequest): Resource<Boolean> {
        val vehicleResponse = vehicleDataSource.vehicleRegister(vehicleRegisterRequest)

        return when (vehicleResponse.success) {
            true -> {
                Resource.Success(true)
            }

            false -> {
                Resource.Error(
                    exception = Exception(vehicleResponse.message ?: "Đã xảy ra sự cố!"),
                    errorData = vehicleResponse.errors
                )
            }
        }
    }

    suspend fun getVehicleById(vehicleId: String): Resource<Vehicle> {
        val vehicleResponse = vehicleDataSource.getVehicleById(vehicleId)

        return when (vehicleResponse.success) {
            true -> {
                Resource.Success(
                    vehicleResponse.data ?: throw Exception("Không tìm thấy thông tin phương tiện")
                )
            }

            false -> {
                Resource.Error(
                    exception = Exception(
                        vehicleResponse.message ?: "Không thể tải thông tin phương tiện"
                    ),
                    errorData = vehicleResponse.errors
                )
            }
        }
    }

    suspend fun updateVehicleById(
        vehicleId: String,
        vehicleUpdateRequest: VehicleUpdateRequest
    ): Resource<Boolean> {
        val updateResponse = vehicleDataSource.updateVehicleById(vehicleId, vehicleUpdateRequest)
        Log.d("VehicleRepository", "updateVehicle: $updateResponse")

        return when (updateResponse.success) {
            true -> {
                Resource.Success(true)
            }

            false -> {
                Resource.Error(
                    exception = Exception(
                        updateResponse.message ?: "Không thể cập nhật phương tiện"
                    ),
                    errorData = updateResponse.errors
                )
            }
        }
    }

    suspend fun deleteVehicle(vehicleId: String): Resource<Boolean> {
        val deleteResponse = vehicleDataSource.deleteVehicle(vehicleId)

        return when (deleteResponse.success) {
            true -> {
                Resource.Success(true)
            }

            false -> {
                Resource.Error(
                    exception = Exception(deleteResponse.message ?: "Không thể xóa phương tiện"),
                    errorData = deleteResponse.errors
                )
            }
        }
    }
}