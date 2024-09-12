package com.example.sosapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sosapp.model.AlertRequest
import com.example.sosapp.repository.AlertRepo
import com.example.sosapp.util.ResultWrapper
import com.example.sosapp.util.apiCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
@HiltViewModel
class CameraViewModel @Inject constructor(private val repo: AlertRepo) : ViewModel() {
    private var isCameraOpened = false
    private val _state = MutableLiveData<AlertUiState>()
    val state: LiveData<AlertUiState> = _state

    fun safeOpenCamera(onComplete: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.Main) {
            var attempts = 0
            while (attempts < 3) {
                try {
                    delay(500)
                    if (!isCameraOpened) {
                        isCameraOpened = true
                        onComplete(true)
                        return@launch
                    } else {
                        onComplete(true)
                        return@launch
                    }
                } catch (e: Exception) {
                    delay(1000)
                }
                attempts++
            }
            onComplete(false)
        }
    }

    // Function to safely close the camera
    fun safeCloseCamera(onComplete: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(NonCancellable) {
                    delay(500)  // Simulating camera closing
                    if (isCameraOpened) {
                        isCameraOpened = false
                    } else {
                        println()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                withContext(Dispatchers.Main) {
                    onComplete()  // Complete action on the main thread
                }
            }
        }
    }
    private val alertExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _state.postValue(
           AlertUiState.ERROR(
                "An Error Occurred,Please try again",
                111
            )
        )
    }


    fun alertPolice(body: AlertRequest) {
        viewModelScope.launch(alertExceptionHandler) {
            _state.postValue(AlertUiState.LOADING(true))
            try {
                when (val data = apiCall {
                    repo.alertPolice(
                        body = body
                    )
                }) {
                    is ResultWrapper.Success -> {
                        _state.postValue(AlertUiState.LOADING(false))
                        _state.postValue(
                            AlertUiState.SUCCESS(
                                data = data.value.data,
                                successResponse = data.value.message
                            )
                        )
                    }

                    is ResultWrapper.Error -> {
                        val errorCode = data.code
                        _state.postValue(AlertUiState.LOADING(false))
                        _state.postValue(AlertUiState.ERROR(data.error, errorCode))

                    }
                }
            }catch (e:Exception){
                _state.postValue(
                    AlertUiState.ERROR(
                        "An Error Occurred,Please try again",
                        111
                    )
                )
            }
        }
    }
}
