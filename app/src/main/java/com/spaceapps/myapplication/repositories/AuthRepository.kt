package com.spaceapps.myapplication.repositories

import com.google.android.gms.tasks.Tasks
import com.google.firebase.messaging.FirebaseMessaging
import com.spaceapps.myapplication.local.AuthTokenStorage
import com.spaceapps.myapplication.models.remote.auth.*
import com.spaceapps.myapplication.models.remote.auth.DeviceRequest.Platform.Android
import com.spaceapps.myapplication.network.AuthorizationApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val api: AuthorizationApi,
    private val storage: AuthTokenStorage
) {

    suspend fun signIn(email: String, password: String) {
        val request = AuthRequest(
            email = email,
            password = password,
            device = DeviceRequest(
                token = Tasks.await(FirebaseMessaging.getInstance().token),
                platform = Android
            )
        )
        api.signIn(request = request).also {
            storage.storeTokens(authToken = it.authToken, refreshToken = it.refreshToken)
        }
    }

    suspend fun signUp(email: String, password: String) {
        val request = AuthRequest(
            email = email,
            password = password,
            device = DeviceRequest(
                token = Tasks.await(FirebaseMessaging.getInstance().token),
                platform = Android
            )
        )
        api.signUp(request = request).also {
            storage.storeTokens(authToken = it.authToken, refreshToken = it.refreshToken)
        }
    }

    suspend fun signInWithGoogle(accessToken: String) {
        val request = SocialSignInRequest(
            accessToken = accessToken,
            device = DeviceRequest(
                token = Tasks.await(FirebaseMessaging.getInstance().token),
                platform = Android
            )
        )
        api.googleSignIn(request = request).also {
            storage.storeTokens(authToken = it.authToken, refreshToken = it.refreshToken)
        }
    }

    suspend fun signInWithFacebook(accessToken: String) {
        val request = SocialSignInRequest(
            accessToken = accessToken,
            device = DeviceRequest(
                token = Tasks.await(FirebaseMessaging.getInstance().token),
                platform = Android
            )
        )
        api.facebookSignIn(request = request).also {
            storage.storeTokens(authToken = it.authToken, refreshToken = it.refreshToken)
        }
    }

    suspend fun signInWithApple(accessToken: String) {
        val request = SocialSignInRequest(
            accessToken = accessToken,
            device = DeviceRequest(
                token = Tasks.await(FirebaseMessaging.getInstance().token),
                platform = Android
            )
        )
        api.appleSignIn(request = request).also {
            storage.storeTokens(authToken = it.authToken, refreshToken = it.refreshToken)
        }
    }

    suspend fun sendResetToken(email: String) {
        api.sendResetToken(request = SendResetTokenRequest(email = email))
    }

    suspend fun verifyResetToken(email: String, token: String) {
        val request = VerifyTokenRequest(email = email, resetToken = token)
        api.verifyResetToken(request = request)
    }

    suspend fun resetPassword(email: String, token: String, password: String) {
        val request =
            ResetPasswordRequest(email = email, resetToken = token, newPassword = password)
        api.resetPassword(request = request)
    }

    suspend fun logOut() {
        val device = DeviceRequest(
            token = Tasks.await(FirebaseMessaging.getInstance().token),
            platform = Android
        )
        api.logOut(device = device)
    }
}
