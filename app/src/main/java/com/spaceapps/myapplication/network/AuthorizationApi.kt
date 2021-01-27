package com.spaceapps.myapplication.network

import com.spaceapps.myapplication.models.AuthTokenResponse
import retrofit2.http.*

interface AuthorizationApi {

    @GET("/authorization/sign-in")
    suspend fun login(
        @Query("userName") userName: String,
        @Query("password") password: String
    ): AuthTokenResponse

    @POST("/authorization/sign-up")
    suspend fun register(
        @Query("userName") userName: String,
        @Query("password") password: String
    ): AuthTokenResponse

    @PUT("/authorization/fcm-token/{token}")
    suspend fun sendFcmToken(@Path("token") token: String)

    @GET("/authorization/refresh-token")
    suspend fun refreshToken(@Query("refresh_token") token: String): AuthTokenResponse

}