package com.spaceapps.myapplication.core.models.remote.profile

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
enum class Platform {
    @Json(name = "Android")
    Android,

    @Json(name = "Ios")
    Ios
}
