package com.spaceapps.myapplication.models.remote.auth

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DeviceRequest(
    @Json(name = "token")
    val token: String,
    @Json(name = "platform")
    val platform: Platform
) {
    @JsonClass(generateAdapter = false)
    enum class Platform {
        @Json(name = "android")
        Android,

        @Json(name = "ios")
        Ios
    }
}
