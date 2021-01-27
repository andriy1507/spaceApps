package com.spaceapps.myapplication.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.joda.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class PostResponse(
    @Json(name = "id")
    val id: Long,
    @Json(name = "title")
    val title: String,
    @Json(name = "text")
    val text: String,
    @Json(name = "created")
    val created: LocalDateTime,
    @Json(name = "isLiked")
    val isLiked: Boolean,
    @Json(name = "likesCount")
    val likesCount: Long,
    @Json(name = "commentsCount")
    val commentsCount: Long
)