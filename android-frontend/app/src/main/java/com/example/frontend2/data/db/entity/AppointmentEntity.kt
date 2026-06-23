package com.example.frontend2.data.db.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "appointments")
data class AppointmentEntity(
    @PrimaryKey val id: Int,
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
    @Embedded(prefix = "vehicle_") val vehicle: VehicleEntity,
    @Embedded(prefix = "center_") val center: CenterEntity
)

@Entity(tableName = "vehicles")
data class VehicleEntity(
    @PrimaryKey val id: Int,
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

@Entity(tableName = "centers")
data class CenterEntity(
    @PrimaryKey val id: Int,
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