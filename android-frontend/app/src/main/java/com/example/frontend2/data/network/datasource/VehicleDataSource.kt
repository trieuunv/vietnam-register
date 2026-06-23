package com.example.frontend2.data.network.datasource

import com.example.frontend2.data.network.api.VehicleApi
import com.example.frontend2.data.network.dto.request.VehicleRegisterRequest
import com.example.frontend2.data.network.dto.request.VehicleUpdateRequest
import com.example.frontend2.data.network.dto.response.ApiResponse
import com.example.frontend2.data.network.dto.response.VehicleResponse
import com.example.frontend2.domain.model.Vehicle

import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class VehicleDataSource @Inject constructor(
    @Named("auth") private val retrofit: Retrofit
) : BaseDataSource() {

    private val vehicleApi: VehicleApi by lazy {
        retrofit.create(VehicleApi::class.java)
    }

    /**
     * Fetches a list of vehicles from the API.
     *
     * This function uses the safeApiCall method from the BaseDataSource class to handle API calls safely.
     * It returns an ApiResponse object containing a list of Vehicle objects.
     *
     * @return An ApiResponse object containing a list of Vehicle objects.
     */
    suspend fun getVehicles(): ApiResponse<List<Vehicle>> {
        return safeApiCall { vehicleApi.getVehicles() }
    }

    /**
     * Fetches a list of vehicle types from the API.
     *
     * This function uses the safeApiCall method from the BaseDataSource class to handle API calls safely.
     * It returns an ApiResponse object containing a list of VehicleResponse objects.
     *
     * @return An ApiResponse object containing a list of VehicleResponse objects.
     */
    suspend fun getVehicleType(): ApiResponse<List<VehicleResponse>> {
        return safeApiCall { vehicleApi.getVehicleType() }
    }

    /**
     * Registers a new vehicle using the API.
     *
     * This function uses the safeApiCall method from the BaseDataSource class to handle API calls safely.
     * It takes a VehicleRegisterRequest object as a parameter and returns an ApiResponse object containing the same request object.
     *
     * @param vehicleRegisterRequest The request object containing the vehicle registration details.
     * @return An ApiResponse object containing the VehicleRegisterRequest object.
     */
    suspend fun vehicleRegister(vehicleRegisterRequest: VehicleRegisterRequest): ApiResponse<VehicleRegisterRequest> {
        return safeApiCall { vehicleApi.vehicleRegister(vehicleRegisterRequest) }
    }

    /**
     * Fetches a vehicle by its ID from the API.
     *
     * This function uses the safeApiCall method from the BaseDataSource class to handle API calls safely.
     * It takes a vehicle ID as a parameter and returns an ApiResponse object containing the Vehicle object.
     *
     * @param vehicleId The ID of the vehicle to fetch.
     * @return An [ApiResponse] object containing the [Vehicle] object.
     */
    suspend fun getVehicleById(vehicleId: String): ApiResponse<Vehicle> {
        return safeApiCall { vehicleApi.getVehicleById(vehicleId) }
    }

    /**
     * Updates a vehicle by its ID using the API.
     *
     * This function uses the safeApiCall method from the BaseDataSource class to handle API calls safely.
     * It takes a vehicle ID and a VehicleUpdateRequest object as parameters and returns an ApiResponse object containing the same request object.
     *
     * @param vehicleId The ID of the vehicle to update.
     * @param vehicleUpdateRequest The request object containing the updated vehicle details.
     * @return An [ApiResponse] object containing the [VehicleUpdateRequest] object.
     */
    suspend fun updateVehicleById(
        vehicleId: String,
        vehicleUpdateRequest: VehicleUpdateRequest
    ): ApiResponse<VehicleUpdateRequest> {
        return safeApiCall { vehicleApi.updateVehicleById(vehicleId, vehicleUpdateRequest) }
    }

    /**
     * Deletes a vehicle by its ID using the API.
     *
     * This function uses the safeApiCall method from the BaseDataSource class to handle API calls safely.
     * It takes a vehicle ID as a parameter and returns an ApiResponse object containing a Boolean value indicating success or failure.
     *
     * @param vehicleId The ID of the vehicle to delete.
     * @return An [ApiResponse] object containing a Boolean value indicating success or failure.
     */
    suspend fun deleteVehicle(vehicleId: String): ApiResponse<Boolean> {
        return safeApiCall { vehicleApi.deleteVehicle(vehicleId) }
    }

}