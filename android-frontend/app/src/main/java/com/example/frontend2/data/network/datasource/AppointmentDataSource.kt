package com.example.frontend2.data.network.datasource

import android.util.Log
import com.example.frontend2.data.network.api.AppointmentApi
import com.example.frontend2.data.network.dto.request.AppointmentCreateRequest
import com.example.frontend2.data.network.dto.response.ApiResponse
import com.example.frontend2.data.network.dto.response.AppointmentCreateResponse
import com.example.frontend2.data.network.dto.response.AppointmentManagerResponse
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named

class AppointmentDataSource @Inject constructor(
    @Named("auth") private val retrofit: Retrofit
) : BaseDataSource() {

    private val appointmentApi: AppointmentApi by lazy {
        retrofit.create(AppointmentApi::class.java)
    }

    /**
     * Create a new appointment.
     *
     * @param appointmentCreateRequest The request body containing appointment details.
     * @return A [Response] containing the API response with the created appointment details.
     */
    suspend fun createAppointment(appointmentCreateRequest: AppointmentCreateRequest): ApiResponse<AppointmentCreateResponse> {
        return safeApiCall {
            val result = appointmentApi.createAppointment(appointmentCreateRequest)
            Log.d("AppointmentDataSource", "createAppointment: $result")
            result
        }
    }

    /**
     * Get all appointments.
     *
     * @return A [Response] containing the API response with a list of appointments.
     */
    suspend fun getAllAppointments(): ApiResponse<List<AppointmentManagerResponse>> {
        return safeApiCall {
            val result = appointmentApi.getAllAppointments()
            Log.d("AppointmentDataSource", "getAllAppointments: $result")
            result
        }
    }

    /**
     * Get all appointments completed.
     *
     * @return A [Response] containing the API response with a list of completed appointments.
     */
    suspend fun getCompletedAppointments(): ApiResponse<List<AppointmentManagerResponse>> {
        return safeApiCall {
            val result = appointmentApi.getCompletedAppointments()
            Log.d("AppointmentDataSource", "getCompletedAppointments: $result")
            result
        }
    }

    /**
     * Get all appointments pending.
     *
     * @return A [Response] containing the API response with a list of pending appointments.
     */
    suspend fun getPendingAppointments(): ApiResponse<List<AppointmentManagerResponse>> {
        return safeApiCall {
            val result = appointmentApi.getPendingAppointments()
            Log.d("AppointmentDataSource", "getPendingAppointments: $result")
            result
        }
    }

    /**
     * Get an appointment by its ID.
     *
     * @param appointmentId The ID of the appointment to retrieve.
     * @return A [Response] containing the API response with the appointment details.
     */
    suspend fun getAppointmentById(appointmentId: String): ApiResponse<AppointmentManagerResponse> {
        return safeApiCall {
            val result = appointmentApi.getAppointmentById(appointmentId)
            Log.d("AppointmentDataSource", "getAppointmentById: $result")
            result
        }
    }

}