package com.spaceapps.myapplication.network

import com.spaceapps.myapplication.models.AuthRequest
import com.spaceapps.myapplication.models.AuthRequest.*
import com.spaceapps.myapplication.models.AuthTokenResponse
import retrofit2.http.*

interface AuthorizationApi {

    @POST("/authorization/sign-in")
    suspend fun signIn(@Body request: AuthRequest): AuthTokenResponse

    @POST("/authorization/sign-up")
    suspend fun signUp(@Body request: AuthRequest): AuthTokenResponse

    @PUT("/authorization/device")
    suspend fun sendFcmToken(@Body device: Device)

    @GET("/authorization/refresh-token")
    suspend fun refreshToken(@Query("refresh_token") token: String): AuthTokenResponse
}
