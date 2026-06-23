package com.example.frontend2.data.db.mapper

import com.example.frontend2.data.db.entity.AppointmentEntity
import com.example.frontend2.data.db.entity.CenterEntity
import com.example.frontend2.data.db.entity.VehicleEntity
import com.example.frontend2.data.network.dto.response.AppointmentManagerResponse

// Extension functions to convert between network and local entities
fun AppointmentManagerResponse.toEntity(): AppointmentEntity {
    return AppointmentEntity(
        id = id,
        vehicleId = vehicleId,
        centerId = centerId,
        appointmentDate = appointmentDate,
        status = status,
        confirmationCode = confirmationCode,
        notes = notes,
        createdAt = createdAt,
        updatedAt = updatedAt,
        deletedAt = deletedAt,
        ownerId = ownerId,
        createdBy = createdBy,
        inspectionId = inspectionId,
        vehicle = vehicle.toEntity(),
        center = center.toEntity()
    )
}

fun com.example.frontend2.data.network.dto.response.Vehicle.toEntity(): VehicleEntity {
    return VehicleEntity(
        id = id,
        ownerId = ownerId,
        vehicleTypeId = vehicleTypeId,
        registrationNumber = registrationNumber,
        chassisNumber = chassisNumber,
        engineNumber = engineNumber,
        brand = brand,
        model = model,
        manufactureYear = manufactureYear,
        color = color,
        seatCount = seatCount,
        firstRegistrationDate = firstRegistrationDate,
        purposeOfUse = purposeOfUse,
        registrationStatus = registrationStatus,
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt,
        deletedAt = deletedAt
    )
}

fun com.example.frontend2.data.network.dto.response.Center.toEntity(): CenterEntity {
    return CenterEntity(
        id = id,
        name = name,
        code = code,
        phone = phone,
        email = email,
        directorName = directorName,
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt,
        deletedAt = deletedAt
    )
}

// Conversion back to response objects if needed
fun AppointmentEntity.toResponse(): AppointmentManagerResponse {
    return AppointmentManagerResponse(
        id = id,
        vehicleId = vehicleId,
        centerId = centerId,
        appointmentDate = appointmentDate,
        status = status,
        confirmationCode = confirmationCode,
        notes = notes,
        createdAt = createdAt,
        updatedAt = updatedAt,
        deletedAt = deletedAt,
        ownerId = ownerId,
        createdBy = createdBy,
        inspectionId = inspectionId,
        vehicle = vehicle.toResponse(),
        center = center.toResponse()
    )
}

fun VehicleEntity.toResponse(): com.example.frontend2.data.network.dto.response.Vehicle {
    return com.example.frontend2.data.network.dto.response.Vehicle(
        id = id,
        ownerId = ownerId,
        vehicleTypeId = vehicleTypeId,
        registrationNumber = registrationNumber,
        chassisNumber = chassisNumber,
        engineNumber = engineNumber,
        brand = brand,
        model = model,
        manufactureYear = manufactureYear,
        color = color,
        seatCount = seatCount,
        firstRegistrationDate = firstRegistrationDate,
        purposeOfUse = purposeOfUse,
        registrationStatus = registrationStatus,
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt,
        deletedAt = deletedAt
    )
}

fun CenterEntity.toResponse(): com.example.frontend2.data.network.dto.response.Center {
    return com.example.frontend2.data.network.dto.response.Center(
        id = id,
        name = name,
        code = code,
        phone = phone,
        email = email,
        directorName = directorName,
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt,
        deletedAt = deletedAt
    )
}