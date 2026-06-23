package com.example.frontend2.domain.model

data class CitizenCardErrorState(
    var errorCitizenId: String = "",
    var errorPlaceOfOrigin: String = "",
    var errorDateOfIssue: String = "",
    var errorPlaceOfIssue: String = "",
    var errorImageUrl: String = ""
)