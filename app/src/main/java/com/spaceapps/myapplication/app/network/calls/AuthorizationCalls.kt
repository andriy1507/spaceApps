package com.spaceapps.myapplication.app.network.calls

import com.spaceapps.myapplication.app.models.remote.auth.*
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

    @POST("/auth/refresh-token")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): AuthTokenResponse

    @POST("/auth/google-sign-in")
    suspend fun googleSignIn(@Body request: SocialSignInRequest): AuthTokenResponse

    @POST("/auth/facebook-sign-in")
    suspend fun facebookSignIn(@Body request: SocialSignInRequest): AuthTokenResponse

    @POST("/auth/apple-sign-in")
    suspend fun appleSignIn(@Body request: SocialSignInRequest): AuthTokenResponse

    @POST("/auth/send-reset-code")
    suspend fun sendResetCode(@Body request: SendResetCodeRequest)

    @POST("/auth/verify-reset-code")
    suspend fun verifyResetCode(@Body request: VerifyCodeRequest)

    @PUT("/auth/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest)
}
