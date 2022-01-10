@file:Suppress("SpellCheckingInspection")

package com.ahmetbabacan.satellites.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.preferencesOf
import androidx.datastore.preferences.core.stringPreferencesKey
import app.cash.turbine.test
import com.ahmetbabacan.satellites.MainCoroutinesRule
import com.ahmetbabacan.satellites.MockUtil
import com.ahmetbabacan.satellites.network.SatelliteService
import com.ahmetbabacan.satellites.ui.detail.DetailRepository
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.squareup.moshi.Moshi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import kotlin.time.Duration

@ExperimentalCoroutinesApi
class DetailRepositoryTest {

    private lateinit var repository: DetailRepository
    private val service: SatelliteService = mock()
    private val dataStore = mock<DataStore<Preferences>>()
    private val moshi: Moshi = Moshi.Builder().build()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesRule = MainCoroutinesRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        repository = DetailRepository(service, dataStore, moshi)
    }


    @Test
    fun getDetailFromNetworkTest() = runBlocking {
        val mockResponse = MockUtil.mockDetailResponse()
        whenever(dataStore.data).thenReturn(flowOf(preferencesOf(intPreferencesKey("dummy") to 1)))
        whenever(service.getSatelliteDetail(3)).thenReturn(Response.success(mockResponse))
        val mockDetail = mockResponse[2]

        repository.satelliteDetail(
            satelliteId = 3,
            onStart = {},
            onSuccess = {},
            onError = {}
        ).test {
            val satellite = awaitItem()
            assertEquals(satellite.cost_per_launch, mockDetail.cost_per_launch)
            assertEquals(satellite.first_flight, mockDetail.first_flight)
            assertEquals(satellite.height, mockDetail.height)
            awaitComplete()
        }
    }

    @Test
    fun getDetailFromDatabaseTest() = runBlocking {
        val mockJsonResponse = MockUtil.mockDetailJson()
        whenever(dataStore.data).thenReturn(flowOf(preferencesOf(stringPreferencesKey("3") to mockJsonResponse)))
        val mockDetail = MockUtil.mockDetailResponse()[2]

        repository.satelliteDetail(
            satelliteId = 3,
            onStart = {},
            onSuccess = {},
            onError = {}
        ).test {
            val satellite = awaitItem()
            assertEquals(satellite.cost_per_launch, mockDetail.cost_per_launch)
            assertEquals(satellite.first_flight, mockDetail.first_flight)
            assertEquals(satellite.height, mockDetail.height)
            awaitComplete()
        }
    }

    @Test
    fun getPositionsTest() = runBlocking {
        val mockResponse = MockUtil.mockPositionsResponse()
        whenever(service.getPositions(1)).thenReturn(Response.success(mockResponse))
        val mockPositions = mockResponse.list[0].positions

        repository.positions(
            satelliteId = 1,
            onStart = {},
            onSuccess = {},
            onError = {}
        ).test(timeout = Duration.Companion.seconds(10)) {
            assertEquals(awaitItem().posX, mockPositions[0].posX, 0.0)
            assertEquals(awaitItem().posX, mockPositions[1].posX, 0.0)
            assertEquals(awaitItem().posX, mockPositions[2].posX, 0.0)
            awaitComplete()
        }
    }

}
