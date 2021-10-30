package com.spaceapps.myapplication.core.models.remote.auth

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class AuthTokenResponse(
    @Json(name = "access_token")
    val accessToken: String,
    @Json(name = "access_token_exp")
    val accessTokenExp: LocalDateTime,
    @Json(name = "refresh_token")
    val refreshToken: String,
    @Json(name = "refresh_token_exp")
    val refreshTokenExp: LocalDateTime
)
