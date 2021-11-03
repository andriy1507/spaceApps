package com.spaceapps.myapplication.core.models.remote.files

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FileResponse(
    @Json(name = "url")
    val url: String,
    @Json(name = "size")
    val size: Long
)