package com.spaceapps.myapplication.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResetPasswordRequest(
    @Json(name = "email")
    val email: String,
    @Json(name = "resetToken")
    val resetToken: String,
    @Json(name = "newPassword")
    val newPassword: String
)
