package com.example.frontend2.data.network.dto.request

import com.google.gson.annotations.SerializedName

data class CitizenCardRequest(
    @SerializedName("citizen_id") val citizenId: String,
    @SerializedName("place_of_origin") val placeOfOrigin: String,
    @SerializedName("date_of_issue") val dateOfIssue: String,
    @SerializedName("place_of_issue") val placeOfIssue: String,
    @SerializedName("image_url") val imageUrl: String
)