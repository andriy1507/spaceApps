package com.spaceapps.myapplication.models.remote.chat

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChatResponse(
    @Json(name = "id")
    val conversationId: String,
    @Json(name = "name")
    val name: String
)
