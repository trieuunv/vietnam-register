package com.example.frontend2.data.repository

import android.util.Log
import com.example.frontend2.data.db.dao.AppointmentDao
import com.example.frontend2.data.db.mapper.toEntity
import com.example.frontend2.data.db.mapper.toResponse
import com.example.frontend2.data.network.datasource.AppointmentDataSource
import com.example.frontend2.data.network.dto.request.AppointmentCreateRequest
import com.example.frontend2.data.network.dto.response.AppointmentCreateResponse
import com.example.frontend2.data.network.dto.response.AppointmentManagerResponse
import com.example.frontend2.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AppointmentRepository @Inject constructor(
    private val appointmentDataSource: AppointmentDataSource,
    private val appointmentDao: AppointmentDao
) {
    suspend fun createAppointment(appointmentCreateRequest: AppointmentCreateRequest): Resource<AppointmentCreateResponse> {
        val appointmentResponse = appointmentDataSource.createAppointment(appointmentCreateRequest)

        return when (appointmentResponse.success) {
            true -> {
                Resource.Success(
                    appointmentResponse.data
                        ?: throw Exception("Không có dữ liệu trả về, vui lòng thử lại sau")
                )
            }

            false -> {
                Resource.Error(
                    exception = Exception(
                        appointmentResponse.message
                            ?: "Không thể tạo lịch hẹn, vui lòng thử lại sau"
                    ),
                    errorData = appointmentResponse.errors
                )
            }
        }
    }

    /**
     * Get all appointments with local caching and immediate local data display
     * @return Flow of cached appointments and updated remote data
     */
    fun getAllAppointments(): Flow<List<AppointmentManagerResponse>> = flow {
        // Immediately emit cached data
        val cachedAppointments = appointmentDao.getAllAppointments()
            .map { entities -> entities.map { it.toResponse() } }
            .first()

        // If we have cached data, emit it first
        if (cachedAppointments.isNotEmpty()) {
            emit(cachedAppointments)
        }

        // Then fetch and update from remote source
        try {
            val appointmentResponse = appointmentDataSource.getAllAppointments()

            when (appointmentResponse.success) {
                true -> {
                    val appointments = appointmentResponse.data
                        ?: throw Exception("Không có dữ liệu trả về, vui lòng thử lại sau")

                    withContext(Dispatchers.IO) {
                        // Replace existing data with new data
                        appointmentDao.clearAllAppointments()
                        appointmentDao.insertAppointments(
                            appointments.map { it.toEntity() }
                        )
                    }

                    // Emit updated data
                    emit(appointments)
                }

                false -> {
                    throw Exception(
                        appointmentResponse.message
                            ?: "Không thể lấy thông tin lịch hẹn, vui lòng thử lại sau"
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("AppointmentRepository", "Error fetching all appointments", e)
            // If fetch fails and no cached data was emitted earlier, throw the error
            if (cachedAppointments.isEmpty()) {
                throw e
            }
        }
    }

    /**
     * Get pending appointments with local caching and immediate local data display
     * @return Flow of cached pending appointments and updated remote data
     */
    fun getPendingAppointments(): Flow<List<AppointmentManagerResponse>> = flow {
        // Immediately emit cached data
        val cachedAppointments = appointmentDao.getPendingAppointments()
            .map { entities -> entities.map { it.toResponse() } }
            .first()

        // If we have cached data, emit it first
        if (cachedAppointments.isNotEmpty()) {
            emit(cachedAppointments)
        }

        // Then fetch and update from remote source
        try {
            val appointmentResponse = appointmentDataSource.getPendingAppointments()

            when (appointmentResponse.success) {
                true -> {
                    val appointments = appointmentResponse.data
                        ?: throw Exception("Không có dữ liệu trả về, vui lòng thử lại sau")

                    withContext(Dispatchers.IO) {
                        // Replace existing data with new data
                        appointmentDao.clearAllAppointments()
                        appointmentDao.insertAppointments(
                            appointments.map { it.toEntity() }
                        )
                    }

                    // Emit updated data
                    emit(appointments)
                }

                false -> {
                    throw Exception(
                        appointmentResponse.message
                            ?: "Không thể lấy thông tin lịch hẹn, vui lòng thử lại sau"
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("AppointmentRepository", "Error fetching pending appointments", e)
            // If fetch fails and no cached data was emitted earlier, throw the error
            if (cachedAppointments.isEmpty()) {
                throw e
            }
        }
    }

    /**
     * Get completed appointments with local caching and immediate local data display
     * @return Flow of cached completed appointments and updated remote data
     */
    fun getCompletedAppointments(): Flow<List<AppointmentManagerResponse>> = flow {
        // Immediately emit cached data
        val cachedAppointments = appointmentDao.getCompletedAppointments()
            .map { entities -> entities.map { it.toResponse() } }
            .first()

        // If we have cached data, emit it first
        if (cachedAppointments.isNotEmpty()) {
            emit(cachedAppointments)
        }

        // Then fetch and update from remote source
        try {
            val appointmentResponse = appointmentDataSource.getCompletedAppointments()

            when (appointmentResponse.success) {
                true -> {
                    val appointments = appointmentResponse.data
                        ?: throw Exception("Không có dữ liệu trả về, vui lòng thử lại sau")

                    withContext(Dispatchers.IO) {
                        // Replace existing data with new data
                        appointmentDao.clearAllAppointments()
                        appointmentDao.insertAppointments(
                            appointments.map { it.toEntity() }
                        )
                    }

                    // Emit updated data
                    emit(appointments)
                }

                false -> {
                    throw Exception(
                        appointmentResponse.message
                            ?: "Không thể lấy thông tin lịch hẹn, vui lòng thử lại sau"
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("AppointmentRepository", "Error fetching completed appointments", e)
            // If fetch fails and no cached data was emitted earlier, throw the error
            if (cachedAppointments.isEmpty()) {
                throw e
            }
        }
    }

    /**
     * Get an appointment by its ID with local caching and immediate local data display
     *
     * @param appointmentId The ID of the appointment to retrieve
     * @return Flow of cached appointment and updated remote data
     */
    fun getAppointmentById(appointmentId: String): Flow<AppointmentManagerResponse> = flow {
        // First check if we have this appointment cached locally
        val cachedAppointment = appointmentDao.getAppointmentById(appointmentId.toString())

        // If we have a cached version, emit it first
        if (cachedAppointment != null) {
            emit(cachedAppointment.toResponse())
        }

        // Then fetch from remote source
        try {
            val appointmentResponse = appointmentDataSource.getAppointmentById(appointmentId)

            when (appointmentResponse.success) {
                true -> {
                    val appointment = appointmentResponse.data
                        ?: throw Exception("Không có dữ liệu trả về, vui lòng thử lại sau")

                    // Cache the fetched appointment
                    withContext(Dispatchers.IO) {
                        appointmentDao.insertAppointments(listOf(appointment.toEntity()))
                    }

                    // Emit updated data
                    emit(appointment)
                }

                false -> {
                    throw Exception(
                        appointmentResponse.message
                            ?: "Không thể lấy thông tin lịch hẹn, vui lòng thử lại sau"
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("AppointmentRepository", "Error fetching appointment by ID: $appointmentId", e)
            // If fetch fails and no cached data was emitted earlier, throw the error
            if (cachedAppointment == null) {
                throw e
            }
        }
    }
}