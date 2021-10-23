package com.spaceapps.myapplication.app.models.remote.notifications

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class NotificationShortResponse(
    @Json(name = "id")
    val id: Int,
    @Json(name = "title")
    val title: String,
    @Json(name = "short_text")
    val shortText: String,
    @Json(name = "viewed")
    val viewed: Boolean,
    @Json(name = "created")
    val created: LocalDateTime
)
