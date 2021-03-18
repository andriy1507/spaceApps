package com.spaceapps.myapplication.models.remote.chat

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class MessageResponse(
    @Json(name = "id")
    val messageId: String,
    @Json(name = "chat_id")
    val conversationId: String,
    @Json(name = "text")
    val messageText: String,
    @Json(name = "date_time")
    val dateTime: LocalDateTime
)
