@file:Suppress("SpellCheckingInspection")

package com.ahmetbabacan.satellites.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.ahmetbabacan.satellites.MainCoroutinesRule
import com.ahmetbabacan.satellites.MockUtil.mockSearchResponse
import com.ahmetbabacan.satellites.network.SatelliteService
import com.ahmetbabacan.satellites.ui.search.SearchRepository
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import kotlin.time.Duration

@ExperimentalCoroutinesApi
class SearchRepositoryTest {

    private lateinit var repository: SearchRepository
    private val service: SatelliteService = mock()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesRule = MainCoroutinesRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        repository = SearchRepository(service)
    }

    @Test
    fun fetchListTest() = runBlocking {
            val mockResponse = mockSearchResponse()
            whenever(service.getSatellites("1")).thenReturn(Response.success(mockResponse))
            val mockModel = mockResponse[0]

            repository.searchSatellites(
                keyword = "1",
                onStart = {},
                onSuccess = {},
                onError = {}
            ).test(timeout = Duration.Companion.seconds(3)) {
                assertEquals(awaitItem()[0].id, mockModel.id)
                awaitComplete()
            }
    }
}
