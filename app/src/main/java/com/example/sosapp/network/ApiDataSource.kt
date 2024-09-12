package com.example.sosapp.network

import com.example.sosapp.model.AlertRequest
import javax.inject.Inject


class ApiDataSource @Inject constructor(private val apiService: ApiService) {
    suspend fun alertPolice(
       body: AlertRequest
    ) = apiService.alertPolice(story = body)

}

