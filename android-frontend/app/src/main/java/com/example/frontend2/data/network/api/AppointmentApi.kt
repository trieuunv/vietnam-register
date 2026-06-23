package com.example.frontend2.data.network.api

import com.example.frontend2.data.network.dto.request.AppointmentCreateRequest
import com.example.frontend2.data.network.dto.response.ApiResponse
import com.example.frontend2.data.network.dto.response.AppointmentCreateResponse
import com.example.frontend2.data.network.dto.response.AppointmentManagerResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AppointmentApi {

    /**
     * Create a new appointment.
     *
     * @param appointmentCreateRequest The request body containing appointment details.
     * @return A [Response] containing the API response with the created appointment details.
     */
    @POST("client/appointment/create")
    suspend fun createAppointment(@Body appointmentCreateRequest: AppointmentCreateRequest): Response<ApiResponse<AppointmentCreateResponse>>

    /**
     * Get the list of appointments.
     *
     * @return A [Response] containing the API response with the list of appointments.
     */
    @GET("client/appointment/all")
    suspend fun getAllAppointments(): Response<ApiResponse<List<AppointmentManagerResponse>>>

    /**
     * Get the list of completed appointments.
     *
     * @return A [Response] containing the API response with the list of completed appointments.
     */
    @GET("client/appointment/completed")
    suspend fun getCompletedAppointments(): Response<ApiResponse<List<AppointmentManagerResponse>>>

    /**
     * Get the list of pending appointments.
     *
     * @return A [Response] containing the API response with the list of pending appointments.
     */
    @GET("client/appointment/pending")
    suspend fun getPendingAppointments(): Response<ApiResponse<List<AppointmentManagerResponse>>>

    /**
     * Get an appointment by its ID.
     *
     * @param appointmentId The ID of the appointment to retrieve.
     * @return A [Response] containing the API response with the appointment details.
     */
    @GET("client/appointment/{appointmentId}")
    suspend fun getAppointmentById(@Path("appointmentId") appointmentId: String): Response<ApiResponse<AppointmentManagerResponse>>


}