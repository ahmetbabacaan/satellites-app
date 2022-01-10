package com.ahmetbabacan.satellites.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.ahmetbabacan.satellites.MainCoroutinesRule
import com.ahmetbabacan.satellites.MockUtil
import com.ahmetbabacan.satellites.data.models.Satellite
import com.ahmetbabacan.satellites.network.SatelliteService
import com.ahmetbabacan.satellites.ui.search.SearchRepository
import com.ahmetbabacan.satellites.ui.search.SearchViewModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    private lateinit var viewModel: SearchViewModel
    private lateinit var searchRepository: SearchRepository
    private val satelliteService: SatelliteService = mock()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesRule = MainCoroutinesRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        searchRepository = SearchRepository(satelliteService)
        viewModel = SearchViewModel(searchRepository)
    }

    @Test
    fun fetchListTest() = runBlocking {
        val mockData = MockUtil.mockSearchResponse()
        whenever(
            satelliteService.getSatellites(null)
        ).thenReturn(Response.success(mockData))

        val observer: Observer<ArrayList<Satellite>> = mock()
        val fetchedData: LiveData<ArrayList<Satellite>> =
            searchRepository.searchSatellites(
                keyword = null,
                onStart = {},
                onSuccess = {},
                onError = {}).asLiveData()
        fetchedData.observeForever(observer)

        viewModel.fetchList()
        delay(2000L)

        verify(observer).onChanged(mockData)
        fetchedData.removeObserver(observer)
    }


}
