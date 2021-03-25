package com.spaceapps.myapplication.network

import com.spaceapps.myapplication.models.remote.profile.ProfileRequest
import com.spaceapps.myapplication.models.remote.profile.ProfileResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProfileApi {

    @GET("/profiles/{profileId}")
    suspend fun getProfile(@Path("profileId") profileId: Int): ProfileResponse

    @PUT("/profiles")
    suspend fun updateProfile(@Body request: ProfileRequest): ProfileResponse
}
