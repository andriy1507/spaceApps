package com.spaceapps.myapplication.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PaginationResponse<T>(
    @Json(name = "page")
    val page: Int,
    @Json(name = "total")
    val total: Long,
    @Json(name = "content")
    val content: List<T>
)