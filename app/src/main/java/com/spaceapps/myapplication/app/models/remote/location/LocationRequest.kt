package com.spaceapps.myapplication.app.models.remote.location

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocationRequest(
    @Json(name = "name")
    val name: String,
    @Json(name = "longitude")
    val longitude: Float,
    @Json(name = "latitude")
    val latitude: Float,
    @Json(name = "altitude")
    val altitude: Float
)
