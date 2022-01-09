package com.ahmetbabacan.satellites.network

import com.ahmetbabacan.satellites.util.Constants
import com.squareup.moshi.JsonDataException
import retrofit2.Response
import java.io.IOException


interface NetworkRepository {

    suspend fun <T : Any> wrapNetworkResult(
        call: suspend () -> Response<T>,
        customErrorMessage: String? = null
    ): NetworkResult<T> {

        return try {
            val response = call.invoke()
            NetworkResult.create(
                response,
                customErrorMessage
            )
        } catch (e: JsonDataException) {
            NetworkResult.ErrorNetworkResult(0, "", customErrorMessage)
        } catch (e: IOException) {
            NetworkResult.ExceptionResult(Constants.noConnectionErrorText)
        } catch (e: Exception) {
            NetworkResult.ExceptionResult(e.message)
        }
    }
}