package com.ahmetbabacan.satellites.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.preferencesOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import com.ahmetbabacan.satellites.MainCoroutinesRule
import com.ahmetbabacan.satellites.MockUtil
import com.ahmetbabacan.satellites.data.models.SatelliteDetail
import com.ahmetbabacan.satellites.network.SatelliteService
import com.ahmetbabacan.satellites.ui.detail.DetailRepository
import com.ahmetbabacan.satellites.ui.detail.DetailViewModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.squareup.moshi.Moshi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class DetailViewModelTest {

    private lateinit var viewModel: DetailViewModel
    private lateinit var detailRepository: DetailRepository
    private val satelliteService: SatelliteService = mock()
    private var handle: SavedStateHandle = mock()
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
        detailRepository = DetailRepository(satelliteService, dataStore, moshi)
        val savedStateHandle = SavedStateHandle()
        savedStateHandle.set("satellite", MockUtil.mockSearchResponse()[1])
        viewModel = DetailViewModel(detailRepository, savedStateHandle)
    }

    @Test
    fun fetchDetailTest() = runBlocking {
        val mockData = MockUtil.mockDetailResponse()
        whenever(satelliteService.getSatelliteDetail(1)).thenReturn(Response.success(mockData))
        whenever(dataStore.data).thenReturn(flowOf(preferencesOf(intPreferencesKey("dummy") to 1)))

        val observer: Observer<SatelliteDetail> = mock()
        val fetchedData: LiveData<SatelliteDetail> =
            detailRepository.satelliteDetail(
                satelliteId = 1,
                onStart = {},
                onSuccess = {},
                onError = {}).asLiveData()
        fetchedData.observeForever(observer)

        viewModel.fetchDetail()
        delay(500L)

        verify(observer).onChanged(mockData[0])
        fetchedData.removeObserver(observer)
    }


}
