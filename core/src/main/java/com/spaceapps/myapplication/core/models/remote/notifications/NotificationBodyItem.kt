package com.spaceapps.myapplication.core.models.remote.notifications

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class NotificationBodyItem(
    @Json(name = "image_url")
    val imageUrl: String?,
    @Json(name = "text")
    val text: String?,
    @Json(name = "index")
    val index: Int,
    @Json(name = "type")
    val type: Type
) : Parcelable {

    @Parcelize
    @JsonClass(generateAdapter = false)
    enum class Type : Parcelable {
        @Json(name = "Text")
        Text,

        @Json(name = "Image")
        Image
    }
}
