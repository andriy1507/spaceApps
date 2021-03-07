package com.spaceapps.myapplication.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class ChatMessage(
    @Json(name = "messageId")
    val messageId: String,
    @Json(name = "conversationId")
    val conversationId: String,
    @Json(name = "messageText")
    val messageText: String,
    @Json(name = "dateTime")
    val dateTime: LocalDateTime
)
