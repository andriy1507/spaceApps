package com.spaceapps.myapplication.app.models.remote.auth

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResetPasswordRequest(
    @Json(name = "email")
    val email: String,
    @Json(name = "reset_code")
    val resetCode: String,
    @Json(name = "new_password")
    val newPassword: String
)
