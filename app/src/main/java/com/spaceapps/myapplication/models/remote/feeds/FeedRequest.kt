package com.spaceapps.myapplication.models.remote.feeds

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FeedRequest(
    @Json(name = "title")
    val title: String,
    @Json(name = "items")
    val items: List<FeedItemDto>
)
