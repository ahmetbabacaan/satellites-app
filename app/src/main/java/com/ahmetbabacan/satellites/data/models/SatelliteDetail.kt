package com.ahmetbabacan.satellites.data.models

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class SatelliteDetail(
    val cost_per_launch: Int,
    val first_flight: String,
    val height: Int,
    val id: Int,
    val mass: Int
) : Parcelable