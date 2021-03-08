package com.spaceapps.myapplication.network

import com.spaceapps.myapplication.models.remote.auth.*
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT

interface AuthorizationApi {

    @POST("/auth/sign-in")
    suspend fun signIn(@Body request: AuthRequest): AuthTokenResponse

    @POST("/auth/sign-up")
    suspend fun signUp(@Body request: AuthRequest): AuthTokenResponse

    @POST("/auth/add-device")
    suspend fun addDevice(@Body device: DeviceRequest)

    @DELETE("/auth/log-out")
    suspend fun logOut(@Body device: DeviceRequest)

    @POST("/auth/refresh-token")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): AuthTokenResponse

    @POST("/auth/google-sign-in")
    suspend fun googleSignIn(@Body request: SocialSignInRequest): AuthTokenResponse

    @POST("/auth/facebook-sign-in")
    suspend fun facebookSignIn(@Body request: SocialSignInRequest): AuthTokenResponse

    @POST("/auth/apple-sign-in")
    suspend fun appleSignIn(@Body request: SocialSignInRequest): AuthTokenResponse

    @POST("/auth/send-reset-token")
    suspend fun sendResetToken(@Body request: SendResetTokenRequest)

    @POST("/auth/verify-reset-token")
    suspend fun verifyResetToken(@Body request: VerifyTokenRequest)

    @PUT("/auth/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest)
}
