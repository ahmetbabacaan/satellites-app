package com.ahmetbabacan.satellites.network

import retrofit2.Response


sealed class NetworkResult<T> {

    companion object {

        fun <T> create(
            response: Response<T>,
            customErrorMessage: String? = null
        ): NetworkResult<T> {
            return if (response.isSuccessful) {
                val body = response.body()
                if (body == null) {
                    EmptyNetworkResult(customErrorMessage)
                } else {
                    SuccessfulNetworkResult(body)
                }
            } else {
                ErrorNetworkResult(
                    response.code(),
                    response.errorBody()?.string() ?: response.message(),
                    customErrorMessage
                )
            }
        }

    }

    data class EmptyNetworkResult<T>(var customErrorMessage: String?) : NetworkResult<T>()

    data class ErrorNetworkResult<T>(
        val errorCode: Int,
        val errorMessage: String,
        val customErrorMessage: String?
    ) : NetworkResult<T>()

    data class SuccessfulNetworkResult<T>(val body: T) : NetworkResult<T>()

    data class ExceptionResult<T>(var errorMessage: String?) : NetworkResult<T>()

}