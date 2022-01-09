package com.ahmetbabacan.satellites.network

import com.ahmetbabacan.satellites.data.models.Satellite
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class MoshiConverter {

    @ToJson
    fun satelliteArrayListToJson(list: ArrayList<Satellite>): List<Satellite> = list

    @FromJson
    fun satelliteArrayListFromJson(list: List<Satellite>): ArrayList<Satellite> = ArrayList(list)
}