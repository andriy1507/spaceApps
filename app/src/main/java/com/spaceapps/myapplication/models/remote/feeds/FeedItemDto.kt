package com.spaceapps.myapplication.models.remote.feeds

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FeedItemDto(
    @Json(name = "text")
    val text: String
)
