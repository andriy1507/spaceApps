package com.spaceapps.myapplication.core.models.remote.profile

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProfileResponse(
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val email: String
)
