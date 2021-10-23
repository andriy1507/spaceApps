package com.spaceapps.myapplication.app.models.remote.auth

import android.os.Build
import com.spaceapps.myapplication.app.models.remote.profile.Platform
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DeviceRequest(
    @Json(name = "token")
    val token: String,
    @Json(name = "installation_id")
    val installationId: String,
    @Json(name = "platform")
    val platform: Platform = Platform.Android,
    @Json(name = "manufacturer")
    val manufacturer: String = Build.MANUFACTURER,
    @Json(name = "model")
    val model: String = Build.MODEL,
    @Json(name = "os_version")
    val osVersion: String = "Android API ${Build.VERSION.SDK_INT}"
)
