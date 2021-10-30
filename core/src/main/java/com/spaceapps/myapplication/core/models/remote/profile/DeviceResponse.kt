package com.spaceapps.myapplication.core.models.remote.profile

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DeviceResponse(
    @Json(name = "id")
    val id: Int,
    @Json(name = "platform")
    val platform: Platform,
    @Json(name = "manufacturer")
    val manufacturer: String,
    @Json(name = "model")
    val model: String,
    @Json(name = "os_version")
    val osVersion: String
)
