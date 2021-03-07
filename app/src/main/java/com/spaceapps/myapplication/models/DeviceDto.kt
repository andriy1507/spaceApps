package com.spaceapps.myapplication.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DeviceDto(
    @Json(name = "token")
    val token: String,
    @Json(name = "platform")
    val platform: Platform
) {
    enum class Platform {
        @Json(name = "android")
        Android,
        @Json(name = "ios")
        Ios
    }
}
