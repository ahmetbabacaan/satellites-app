package com.ahmetbabacan.satellites.ui.search

import androidx.annotation.WorkerThread
import com.ahmetbabacan.satellites.data.models.Satellite
import com.ahmetbabacan.satellites.data.responses.ListResponse
import com.ahmetbabacan.satellites.network.NetworkRepository
import com.ahmetbabacan.satellites.network.NetworkResult
import com.ahmetbabacan.satellites.network.SatelliteService
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val satelliteService: SatelliteService
) :
    NetworkRepository {

    @WorkerThread
    fun searchSatellites(
        keyword: String?,
        onStart: () -> Unit,
        onSuccess: () -> Unit,
        onError: (String?) -> Unit
    ) = flow<ArrayList<Satellite>> {

        delay(1000)

        val result: NetworkResult<ArrayList<Satellite>> = wrapNetworkResult(call = {
            satelliteService.getSatellites(query = keyword)
        })

        when (result) {
            is NetworkResult.SuccessfulNetworkResult -> {
                if (keyword == null) {
                    emit(result.body)
                } else {
                    emit(ArrayList(result.body.filter {
                        it.name.lowercase().contains(keyword.lowercase())
                    }))
                }
                onSuccess()
            }
            is NetworkResult.EmptyNetworkResult -> onError(result.customErrorMessage)
            is NetworkResult.ErrorNetworkResult -> {
                try {
                    val moshi = Moshi.Builder().build()
                    val jsonAdapter: JsonAdapter<ListResponse> =
                        moshi.adapter(ListResponse::class.java)
                    jsonAdapter.fromJson(result.errorMessage)?.let { emit(it) }

                } catch (e: Exception) {
                    onError(result.errorMessage)
                }
            }
            is NetworkResult.ExceptionResult -> onError(result.errorMessage)
        }

    }.onStart { onStart() }.flowOn(Dispatchers.IO)
}