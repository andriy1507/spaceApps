package com.spaceapps.myapplication.models.remote.uploads

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class UploadResponse(
    @Json(name = "name")
    val name: String,
    @Json(name = "url")
    val url: String,
    @Json(name = "type")
    val type: Type
) {

    @JsonClass(generateAdapter = false)
    enum class Type {
        @Json(name = "image")
        Image,

        @Json(name = "video")
        Video,

        @Json(name = "audio")
        Audio,

        @Json(name = "file")
        File
    }
}