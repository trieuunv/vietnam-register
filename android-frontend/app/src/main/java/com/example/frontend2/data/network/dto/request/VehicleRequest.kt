package com.example.frontend2.data.network.dto.request

import com.google.gson.annotations.SerializedName

data class VehicleRegisterRequest(
    @SerializedName("vehicle_type_id")
    val vehicleTypeId: String,

    @SerializedName("registration_number")
    val registrationNumber: String,

    @SerializedName("chassis_number")
    val chassisNumber: String,

    @SerializedName("engine_number")
    val engineNumber: String,

    val brand: String,
    val model: String,

    @SerializedName("manufacture_year")
    val manufactureYear: String,

    val color: String,

    @SerializedName("seat_count")
    val seatCount: String,

    @SerializedName("first_registration_date")
    val firstRegistrationDate: String,

    @SerializedName("purpose_of_use")
    val purposeOfUse: String
)

data class VehicleUpdateRequest(
    @SerializedName("vehicle_type_id")
    val vehicleTypeId: String,

    @SerializedName("registration_number")
    val registrationNumber: String,

    @SerializedName("chassis_number")
    val chassisNumber: String,

    @SerializedName("engine_number")
    val engineNumber: String,

    val brand: String,
    val model: String,

    @SerializedName("manufacture_year")
    val manufactureYear: String,

    val color: String,

    @SerializedName("seat_count")
    val seatCount: String,

    @SerializedName("purpose_of_use")
    val purposeOfUse: String
)