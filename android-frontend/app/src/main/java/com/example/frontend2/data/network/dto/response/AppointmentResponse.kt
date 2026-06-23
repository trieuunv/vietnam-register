package com.example.frontend2.data.network.dto.response

data class AppointmentCreateResponse(
    val vehicleId: String,
    val centerId: String,
    val appointmentDate: String,
    val notes: String,
    val ownerId: Int,
    val createdBy: Int,
    val status: String,
    val confirmationCode: String,
    val createdAt: String,
    val updatedAt: String,
    val id: String
)

data class AppointmentManagerResponse(
    val id: Int,
    val vehicleId: Int,
    val centerId: Int,
    val appointmentDate: String,
    val status: String,
    val confirmationCode: String,
    val notes: String?,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?,
    val ownerId: Int,
    val createdBy: Int,
    val inspectionId: Int?,
    val vehicle: Vehicle,
    val center: Center
)

data class Vehicle(
    val id: Int,
    val ownerId: Int,
    val vehicleTypeId: Int,
    val registrationNumber: String,
    val chassisNumber: String,
    val engineNumber: String,
    val brand: String,
    val model: String,
    val manufactureYear: Int,
    val color: String,
    val seatCount: Int,
    val firstRegistrationDate: String,
    val purposeOfUse: String,
    val registrationStatus: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?
)

data class Center(
    val id: Int,
    val name: String,
    val code: String,
    val phone: String,
    val email: String,
    val directorName: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?
)