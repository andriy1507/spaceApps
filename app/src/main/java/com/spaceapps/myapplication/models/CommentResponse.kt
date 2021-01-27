package com.spaceapps.myapplication.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class CommentResponse(
    @Json(name = "id")
    val id: Long,
    @Json(name = "postId")
    val postId: Long,
    @Json(name = "userId")
    val userId: Long,
    @Json(name = "text")
    val text: String
)