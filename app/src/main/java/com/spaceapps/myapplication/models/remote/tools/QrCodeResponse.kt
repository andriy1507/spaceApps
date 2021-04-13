package com.spaceapps.myapplication.models.remote.tools

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class QrCodeResponse(
    @Json(name = "encoded_image")
    val encodedImage: String,
    @Json(name = "width")
    val width: Int,
    @Json(name = "height")
    val height: Int
)