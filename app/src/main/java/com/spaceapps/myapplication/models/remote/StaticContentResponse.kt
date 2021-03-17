package com.spaceapps.myapplication.models.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class StaticContentResponse(
    @Json(name = "content")
    val content: String
)