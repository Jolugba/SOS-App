package com.example.sosapp.repository

import com.example.sosapp.model.AlertRequest
import com.example.sosapp.network.ApiDataSource
import javax.inject.Inject


class AlertRepo @Inject constructor(
    private val apiDataSource: ApiDataSource,
) {
    suspend fun alertPolice(
        body: AlertRequest,
    ) = apiDataSource.alertPolice(
      body= body
    )


}