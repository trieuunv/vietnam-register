package com.example.frontend2.data.network.dto.request

class AppointmentCreateRequest(
    val vehicle_id: String,
    val center_id: String,
    val appointment_date: String,
    val notes: String
)