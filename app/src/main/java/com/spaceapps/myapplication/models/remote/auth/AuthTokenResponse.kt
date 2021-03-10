package com.spaceapps.myapplication.models.remote.auth

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class AuthTokenResponse(
    @Json(name = "authToken")
    val authToken: String,
    @Json(name = "authTokenExp")
    val authTokenExp: LocalDateTime,
    @Json(name = "refreshToken")
    val refreshToken: String,
    @Json(name = "refreshTokenExp")
    val refreshTokenExp: LocalDateTime
)
