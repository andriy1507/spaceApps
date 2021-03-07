package com.spaceapps.myapplication.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class SocialSignInRequest(
    @Json(name = "authToken")
    val authToken: String,
    @Json(name = "device")
    val device: DeviceDto
)
