package com.spaceapps.myapplication.models.remote.chat

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MessageRequest(
    @Json(name = "messageText")
    val messageText: String,
)
