package com.spaceapps.myapplication.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PushNotification(
    @Json(name = "title")
    val title: String?,
    @Json(name = "text")
    val text: String?,
    @Json(name = "imageUrl")
    val imageUrl: String?,
    @Json(name = "type")
    val type: PushNotificationType
) {
    enum class PushNotificationType {
        @Json(name = "0")
        POST,
        @Json(name = "1")
        MESSAGE,
        @Json(name = "2")
        INFO
    }
}
