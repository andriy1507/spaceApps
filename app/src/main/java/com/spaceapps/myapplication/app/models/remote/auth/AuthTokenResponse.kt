package com.spaceapps.myapplication.app.models.remote.auth

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class AuthTokenResponse(
    @Json(name = "auth_token")
    val authToken: String,
    @Json(name = "auth_token_exp")
    val authTokenExp: LocalDateTime,
    @Json(name = "refresh_token")
    val refreshToken: String,
    @Json(name = "refresh_token_exp")
    val refreshTokenExp: LocalDateTime
)
