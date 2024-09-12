package com.example.sosapp.model

data class AlertRequest(
    val image: String,
    val location: Location,
    val phoneNumbers: List<String>
)
