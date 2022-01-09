package com.ahmetbabacan.satellites.network

import com.ahmetbabacan.satellites.data.models.Satellite
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface SatelliteService {

    @Headers("mock:true")
    @GET("list")
    suspend fun getSatellites(@Query("query") query: String?): Response<ArrayList<Satellite>>
}
