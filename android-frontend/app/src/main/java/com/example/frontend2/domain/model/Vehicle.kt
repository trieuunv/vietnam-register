package com.example.frontend2.domain.model

data class Vehicle(
    val id: Int,
    val vehicleTypeId: Int? = null,
    val vehicleTypeName: String? = null,
    val registrationNumber: String,
    val chassisNumber: String? = null,
    val engineNumber: String? = null,
    val brand: String,
    val model: String? = null,
    val manufactureYear: Int? = null,
    val color: String,
    val seatCount: Int? = null,
    val firstRegistrationDate: String? = null,
    val purposeOfUse: String? = null,
    val registrationStatus: String? = null,
    val status: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

data class VehicleErrorState(
    var errorVehicleTypeId: String = "",
    var errorRegistrationNumber: String = "",
    var errorChassisNumber: String = "",
    var errorEngineNumber: String = "",
    var errorBrand: String = "",
    var errorModel: String = "",
    var errorManufactureYear: String = "",
    var errorColor: String = "",
    var errorSeatCount: String = "",
    var errorFirstRegistrationDate: String = "",
    var errorPurposeOfUse: String = "",
)