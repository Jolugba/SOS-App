package com.example.sosapp.network

import com.example.sosapp.model.AlertRequest
import com.example.sosapp.model.AlertResponse
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiService {
    @POST("create")
    suspend fun alertPolice(
        @Body story: AlertRequest,
    ): AlertResponse
}


