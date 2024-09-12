package com.example.sosapp.model

data class AlertResponse(
    val `data`: AlertData,
    val message: String,
    val status: String
)
data class AlertData(
    val id: Int,
    val image: String,
    val location: Location,
    val phoneNumbers: List<String>
)