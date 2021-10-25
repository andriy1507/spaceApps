package com.spaceapps.myapplication.app.models.remote.locations

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class LocationResponse(
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "longitude")
    val longitude: Float,
    @Json(name = "latitude")
    val latitude: Float,
    @Json(name = "created")
    val created: LocalDateTime,
    @Json(name = "altitude")
    val altitude: Float
)
