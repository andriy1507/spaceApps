package com.spaceapps.myapplication.network

import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProfileApi {

    @GET("/profile/{profileId}")
    suspend fun getProfile(@Path("profileId") profileId: Int)

    @PUT("/profile/edit")
    suspend fun updateProfile()

}