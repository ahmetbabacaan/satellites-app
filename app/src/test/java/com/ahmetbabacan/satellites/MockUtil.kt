package com.ahmetbabacan.satellites

import com.ahmetbabacan.satellites.data.models.*

object MockUtil {

    fun mockSearchResponse() = arrayListOf(
        Satellite(false, 1, "Starship-1"),
        Satellite(true, 2, "Dragon-1"),
        Satellite(true, 3, "Starship-2"),
    )

    fun mockDetailResponse() = arrayListOf(
        SatelliteDetail(7200000, "2021-12-01", 118, 1, 1167000),
        SatelliteDetail(8300000, "2020-10-03", 110, 2, 1135000),
        SatelliteDetail(6000000, "2019-09-14", 132, 3, 1435000)
    )

    fun mockDetailJson() =
        "{\"id\":3,\"cost_per_launch\":6000000,\"first_flight\":\"2019-09-14\",\"height\":132,\"mass\":1435000}"

    fun mockPositionsResponse() = PositionResponse(
        listOf(
            PositionOfSatellite(
                "1",
                listOf(
                    Position(0.864328541, 0.646450811),
                    Position(0.459465488, 0.323434385),
                    Position(0.213733842, 0.239480213)
                )
            )
        )
    )
}
