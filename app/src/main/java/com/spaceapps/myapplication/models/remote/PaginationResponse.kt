package com.spaceapps.myapplication.models.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PaginationResponse<T>(
    @Json(name = "page")
    val page: Int,
    @Json(name = "total")
    val total: Long,
    @Json(name = "data")
    val data: List<T>,
    @Json(name = "last")
    val isLast: Boolean
)
