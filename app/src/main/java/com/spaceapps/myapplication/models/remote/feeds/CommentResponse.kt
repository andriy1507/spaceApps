package com.spaceapps.myapplication.models.remote.feeds

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommentResponse(
    @Json(name = "text")
    val id: Int,
    @Json(name = "text")
    val text: String
)
