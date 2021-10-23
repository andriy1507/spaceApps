package com.spaceapps.myapplication.app.models.remote.notifications

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
@JsonClass(generateAdapter = true)
data class NotificationFullResponse(
    @Json(name = "id")
    val id: Int,
    @Json(name = "title")
    val title: String,
    @Json(name = "body")
    val body: List<NotificationBodyItem>,
    @Json(name = "viewed")
    val viewed: Boolean,
    @Json(name = "created")
    val created: LocalDateTime
) : Parcelable
