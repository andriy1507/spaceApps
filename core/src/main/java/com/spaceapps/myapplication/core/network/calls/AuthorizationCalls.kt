package com.spaceapps.myapplication.core.network.calls

import com.spaceapps.myapplication.core.models.remote.auth.*
import retrofit2.http.*

interface AuthorizationCalls {

    @POST("/auth/sign-in")
    suspend fun signIn(@Body request: AuthRequest): AuthTokenResponse

    @POST("/auth/sign-up")
    suspend fun signUp(@Body request: AuthRequest): AuthTokenResponse

    @POST("/auth/add-device")
    suspend fun addDevice(@Body device: DeviceRequest)

    @DELETE("/auth/log-out/{installationId}")
    suspend fun logOut(@Path("installationId") installationId: String)

    @POST("/auth/google-sign-in")
    suspend fun googleSignIn(@Body request: SocialSignInRequest): AuthTokenResponse

    @POST("/auth/facebook-sign-in")
    suspend fun facebookSignIn(@Body request: SocialSignInRequest): AuthTokenResponse

    @POST("/auth/apple-sign-in")
    suspend fun appleSignIn(@Body request: SocialSignInRequest): AuthTokenResponse

    @GET("/auth/refresh-token/{refreshToken}")
    suspend fun refreshToken(@Path("refreshToken") refreshToken: String): AuthTokenResponse

    @POST("/auth/send-reset-code/{email}")
    suspend fun sendResetCode(@Path("email") email: String)

    @GET("/auth/verify-reset-code/{email}/{resetCode}")
    suspend fun verifyResetCode(@Path("email") email: String, @Path("resetCode") resetCode: String)

    @PUT("/auth/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest)
}
