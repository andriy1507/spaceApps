package com.spaceapps.myapplication.models.remote.feeds

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class FeedFullResponse(
    @Json(name = "id")
    val id: Int,
    @Json(name = "title")
    val title: String,
    @Json(name = "items")
    val items: List<FeedItemDto>,
    @Json(name = "created")
    val created:LocalDateTime
)
