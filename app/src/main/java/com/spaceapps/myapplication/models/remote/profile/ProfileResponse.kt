package com.spaceapps.myapplication.models.remote.profile

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.LocalDate

@JsonClass(generateAdapter = true)
data class ProfileResponse(
    @Json(name = "id")
    val id: Int,
    @Json(name = "email")
    val email: String,
    @Json(name = "first_name")
    val firstName: String? = null,
    @Json(name = "last_name")
    val lastName: String? = null,
    @Json(name = "image_url")
    val imageUrl: String? = null,
    @Json(name = "birth_date")
    val birthDate: LocalDate? = null,
    @Json(name = "address")
    val address: String? = null,
    @Json(name = "city")
    val city: String? = null,
    @Json(name = "region")
    val region: String? = null,
    @Json(name = "country")
    val country: String? = null,
    @Json(name = "zip_code")
    val zipCode: String? = null,
    @Json(name = "phone")
    val phone: String? = null
)