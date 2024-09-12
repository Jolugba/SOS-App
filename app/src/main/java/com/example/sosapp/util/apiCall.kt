package com.example.sosapp.util

import android.os.Parcelable
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.parcelize.Parcelize
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

suspend fun <T> apiCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    apiCall: suspend () -> T
): ResultWrapper<T> {
    return withContext(dispatcher) {
        try {
            ResultWrapper.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            throwable.printStackTrace()

            val errorMessage = when (throwable) {
                is UnknownHostException -> "No internet connection. Please check your network settings."
                is SocketTimeoutException -> "Request timed out. Please try again later."
                is IOException -> {
                    if (throwable.message?.contains("Unable to resolve host") == true) {
                        "No internet connection. Please check your network settings."
                    } else {
                        "Network error: ${throwable.message ?: "Not connected to the internet"}"
                    }
                }
                is HttpException -> {
                    val errorBody = throwable.response()?.errorBody()?.string()
                    val errorResponse = try {
                        Gson().fromJson(errorBody, APIErrorResponse::class.java)
                    } catch (e: Exception) {
                        null
                    }
                    errorResponse?.message ?: "An error occurred on the server."
                }
                else -> throwable.localizedMessage ?: "An unknown error occurred."
            }
            ResultWrapper.Error(null, errorMessage)
        }
    }
}




sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T): ResultWrapper<T>()
    data class Error(val code: Int? = null, val error: String): ResultWrapper<Nothing>()
}

@Parcelize
data class APIErrorResponse(
        val message: String
) : Parcelable
