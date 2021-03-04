package com.spaceapps.myapplication.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthRequest(
    @Json(name = "email")
    val email: String,
    @Json(name = "password")
    val password: String,
    @Json(name = "device")
    val device: Device
) {
    @JsonClass(generateAdapter = true)
    data class Device(
        @Json(name = "token")
        val token: String,
        @Json(name = "platform")
        val platform: Platform
    )

    enum class Platform {
        @Json(name = "android")
        Android,

        @Json(name = "ios")
        iOS
    }
}
