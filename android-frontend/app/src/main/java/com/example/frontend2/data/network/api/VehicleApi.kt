package com.example.frontend2.data.network.api

import com.example.frontend2.data.network.dto.request.VehicleRegisterRequest
import com.example.frontend2.data.network.dto.request.VehicleUpdateRequest
import com.example.frontend2.data.network.dto.response.ApiResponse
import com.example.frontend2.data.network.dto.response.VehicleResponse
import com.example.frontend2.domain.model.Vehicle
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface VehicleApi {

    /**
     * @return List of vehicles
     */
    @GET("client/vehicle/")
    suspend fun getVehicles(): Response<ApiResponse<List<Vehicle>>>

    /**
     * @return List of vehicle types
     */
    @GET("common/vehicle-type")
    suspend fun getVehicleType(): Response<ApiResponse<List<VehicleResponse>>>

    /**
     * Registration of a vehicle
     *
     * @param vehicleRegisterRequest The request body containing the vehicle registration details
     * @return A [Response] containing the [ApiResponse] with the vehicle registration details
     */
    @POST("client/vehicle/register")
    suspend fun vehicleRegister(@Body vehicleRegisterRequest: VehicleRegisterRequest): Response<ApiResponse<VehicleRegisterRequest>>

    /**
     * Retrieves a vehicle by its ID
     *
     * @param vehicleId The ID of the vehicle to retrieve
     * @return A [Response] containing the [ApiResponse] with the vehicle details
     */
    @GET("client/vehicle/{vehicleId}")
    suspend fun getVehicleById(@Path("vehicleId") vehicleId: String): Response<ApiResponse<Vehicle>>

    /**
     * Updates a vehicle by its ID
     *
     * @param vehicleId The ID of the vehicle to update
     * @param vehicleUpdateRequest The request body containing the updated vehicle details
     * @return A [Response] containing the [ApiResponse] with the updated vehicle details
     */
    @PUT("client/vehicle/{vehicleId}")
    suspend fun updateVehicleById(
        @Path("vehicleId") vehicleId: String,
        @Body vehicleUpdateRequest: VehicleUpdateRequest
    ): Response<ApiResponse<VehicleUpdateRequest>>

    /**
     * Deletes a vehicle by its ID
     *
     * @param vehicleId The ID of the vehicle to delete
     * @return A [Response] containing the [ApiResponse] with the deletion status
     */
    @DELETE("client/vehicle/{vehicleId}")
    suspend fun deleteVehicle(@Path("vehicleId") vehicleId: String): Response<ApiResponse<Boolean>>

}