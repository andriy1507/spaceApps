package com.spaceapps.myapplication.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VerifyTokenRequest(
    @Json(name = "email")
    val email: String,
    @Json(name = "resetToken")
    val resetToken: String
)
