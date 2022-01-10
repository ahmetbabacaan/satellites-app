package com.ahmetbabacan.satellites.network

import com.ahmetbabacan.satellites.MainCoroutinesRule
import com.ahmetbabacan.satellites.data.models.Satellite
import com.ahmetbabacan.satellites.data.models.SatelliteDetail
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class SatelliteServiceTest : ApiAbstract<SatelliteService>() {

    private lateinit var service: SatelliteService

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesRule = MainCoroutinesRule()

    @Before
    fun initService() {
        service = createService(SatelliteService::class.java)
    }

    @Test
    fun searchTest() = runBlocking {
        enqueueResponse("/list.json")
        val response = service.getSatellites("")
        val responseBody = requireNotNull(response.body()) as ArrayList<Satellite>
        mockWebServer.takeRequest()

        assertThat(responseBody[1].id, `is`(2))
        assertThat(
            responseBody[1].name,
            `is`("Dragon-1")
        )
        assertThat(responseBody[2].active, `is`(true))
    }

    @Throws(IOException::class)
    @Test
    fun detailTest() = runBlocking {
        enqueueResponse("/detail.json")
        val response = service.getSatelliteDetail(1)
        val responseBody = requireNotNull(response.body()) as ArrayList<SatelliteDetail>
        mockWebServer.takeRequest()

        assertThat(responseBody[1].cost_per_launch, `is`(8300000))
        assertThat(responseBody[1].mass, `is`(1135000))
    }
}
