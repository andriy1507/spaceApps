package com.spaceapps.myapplication.core.models.remote.auth

import com.spaceapps.myapplication.core.models.remote.profile.Platform
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DeviceRequest(
    @Json(name = "token")
    val token: String,
    @Json(name = "installation_id")
    val installationId: String,
    @Json(name = "platform")
    val platform: Platform = Platform.Android,
    @Json(name = "manufacturer")
    val manufacturer: String,
    @Json(name = "model")
    val model: String,
    @Json(name = "os_version")
    val osVersion: String
)
