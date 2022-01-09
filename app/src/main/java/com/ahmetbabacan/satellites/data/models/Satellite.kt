package com.ahmetbabacan.satellites.data.models

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Satellite(
    val active: Boolean,
    val id: Int,
    val name: String,
) : Parcelable