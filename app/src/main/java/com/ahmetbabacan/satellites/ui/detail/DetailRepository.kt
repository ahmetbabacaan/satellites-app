package com.ahmetbabacan.satellites.ui.detail

import androidx.annotation.WorkerThread
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ahmetbabacan.satellites.data.responses.DetailResponse
import com.ahmetbabacan.satellites.data.models.Position
import com.ahmetbabacan.satellites.data.models.PositionResponse
import com.ahmetbabacan.satellites.data.models.SatelliteDetail
import com.ahmetbabacan.satellites.network.SatelliteService
import com.ahmetbabacan.satellites.network.NetworkRepository
import com.ahmetbabacan.satellites.network.NetworkResult
import com.ahmetbabacan.satellites.util.Constants.emptyErrorText
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class DetailRepository @Inject constructor(
    private val satelliteService: SatelliteService,
    private val datastore: DataStore<Preferences>,
    private val moshi: Moshi
) :
    NetworkRepository {

    @WorkerThread
    fun satelliteDetail(
        satelliteId: Int,
        onStart: () -> Unit,
        onSuccess: () -> Unit,
        onError: (String?) -> Unit
    ) = flow<SatelliteDetail> {

        val savedSatelliteFlow = datastore.data.map { preferences ->
            preferences[stringPreferencesKey(satelliteId.toString())]
        }
        val savedSatellite : String? = savedSatelliteFlow.first()
        if (savedSatellite != null) {
            val jsonAdapter: JsonAdapter<SatelliteDetail> =
                moshi.adapter(SatelliteDetail::class.java)
            jsonAdapter.fromJson(savedSatellite.toString())?.let { emit(it) }
            onSuccess()
        } else {
            val result: NetworkResult<ArrayList<SatelliteDetail>> = wrapNetworkResult(call = {
                satelliteService.getSatelliteDetail(id = satelliteId)
            }, emptyErrorText)

            when (result) {
                is NetworkResult.SuccessfulNetworkResult -> {
                    val satelliteDetail = result.body.first { it.id == satelliteId }
                    datastore.edit { preferences ->
                        val jsonAdapter: JsonAdapter<SatelliteDetail> =
                            moshi.adapter(SatelliteDetail::class.java)
                        val satelliteKey = stringPreferencesKey(satelliteDetail.id.toString())
                        preferences[satelliteKey] = jsonAdapter.toJson(satelliteDetail)
                    }
                    emit(satelliteDetail)
                    onSuccess()
                }
                is NetworkResult.EmptyNetworkResult -> onError(result.customErrorMessage)
                is NetworkResult.ErrorNetworkResult -> {
                    try {
                        val jsonAdapter: JsonAdapter<DetailResponse> =
                            moshi.adapter(DetailResponse::class.java)
                        jsonAdapter.fromJson(result.errorMessage)
                            ?.let { detailResponse -> emit(detailResponse.first { it.id == satelliteId }) }

                    } catch (e: Exception) {
                        onError(result.errorMessage)
                    }
                }
                is NetworkResult.ExceptionResult -> onError(result.errorMessage)
            }
        }

    }.onStart { onStart() }.flowOn(Dispatchers.IO)

    @WorkerThread
    fun positions(
        satelliteId: Int,
        onStart: () -> Unit,
        onSuccess: () -> Unit,
        onError: (String?) -> Unit
    ) = flow<Position> {

        val result: NetworkResult<PositionResponse> = wrapNetworkResult(call = {
            satelliteService.getPositions(id = satelliteId)
        }, emptyErrorText)

        when (result) {
            is NetworkResult.SuccessfulNetworkResult -> {
                val positionsOfSatellite = result.body.list.first { it.id.toInt() == satelliteId }
                positionsOfSatellite.positions.forEach {
                    emit(it)
                    delay(3000)
                }
                onSuccess()
            }
            is NetworkResult.EmptyNetworkResult -> onError(result.customErrorMessage)
            is NetworkResult.ErrorNetworkResult -> onError(result.errorMessage)
            is NetworkResult.ExceptionResult -> onError(result.errorMessage)
        }

    }.onStart { onStart() }.flowOn(Dispatchers.IO)

}