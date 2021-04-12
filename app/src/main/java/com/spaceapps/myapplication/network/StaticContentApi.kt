package com.spaceapps.myapplication.network

import com.spaceapps.myapplication.models.remote.StaticContentResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface StaticContentApi {

    @GET("/static/{type}")
    suspend fun getStaticContent(@Path("type") type: String): StaticContentResponse
}
