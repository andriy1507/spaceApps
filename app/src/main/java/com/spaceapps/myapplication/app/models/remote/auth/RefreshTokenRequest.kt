package com.spaceapps.myapplication.app.models.remote.auth

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class RefreshTokenRequest(
    @Json(name = "refresh_token")
    val refreshToken: String,
    @Json(name = "device")
    val device: DeviceRequest
)
