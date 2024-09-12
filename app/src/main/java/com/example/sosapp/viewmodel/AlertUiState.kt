package com.example.sosapp.viewmodel

import com.example.sosapp.model.AlertData

sealed class AlertUiState {

    data class LOADING(val loading: Boolean = false) : AlertUiState()

    data class SUCCESS(
        val data: AlertData,
        val successResponse: String?
    ) :AlertUiState()
    data class ERROR(val errorMessage: String?, val errorCode: Int?) : AlertUiState()


}
