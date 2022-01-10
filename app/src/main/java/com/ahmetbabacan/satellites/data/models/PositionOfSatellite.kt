package com.ahmetbabacan.satellites.data.models

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class PositionOfSatellite(
    val id: String,
    val positions: List<Position>
) : Parcelable