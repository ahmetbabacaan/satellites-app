package com.ahmetbabacan.satellites.network

import com.ahmetbabacan.satellites.data.models.PositionResponse
import com.ahmetbabacan.satellites.data.models.Satellite
import com.ahmetbabacan.satellites.data.models.SatelliteDetail
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface SatelliteService {

    @Headers("mock:true")
    @GET("list")
    suspend fun getSatellites(@Query("query") query: String?): Response<ArrayList<Satellite>>

    @Headers("mock:true")
    @GET("detail")
    suspend fun getSatelliteDetail(@Query("id") id: Int): Response<ArrayList<SatelliteDetail>>

    @Headers("mock:true")
    @GET("positions")
    suspend fun getPositions(@Query("id") id: Int): Response<PositionResponse>
}
