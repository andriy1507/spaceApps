package com.spaceapps.myapplication.repositories.auth

import com.google.android.gms.tasks.Tasks
import com.google.firebase.messaging.FirebaseMessaging
import com.spaceapps.myapplication.local.AuthTokenStorage
import com.spaceapps.myapplication.models.remote.auth.*
import com.spaceapps.myapplication.models.remote.auth.DeviceRequest.Platform.Android
import com.spaceapps.myapplication.network.AuthorizationApi
import com.spaceapps.myapplication.utils.Error
import com.spaceapps.myapplication.utils.Success
import com.spaceapps.myapplication.utils.request
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val api: AuthorizationApi,
    private val storage: AuthTokenStorage
) {

    suspend fun signIn(email: String, password: String): SignInResult {
        val request = AuthRequest(
            email = email,
            password = password,
            device = DeviceRequest(
                token = Tasks.await(FirebaseMessaging.getInstance().token),
                platform = Android
            )
        )
        return when (val response = request { api.signIn(request = request) }) {
            is Success -> {
                storage.storeTokens(
                    authToken = response.data.authToken,
                    refreshToken = response.data.refreshToken
                )
                SignInResult.Success
            }
            is Error -> SignInResult.Failure
        }
    }

    suspend fun signUp(email: String, password: String): SignUpResult {
        val request = AuthRequest(
            email = email,
            password = password,
            device = DeviceRequest(
                token = Tasks.await(FirebaseMessaging.getInstance().token),
                platform = Android
            )
        )
        return when (val response = request { api.signUp(request = request) }) {
            is Success -> {
                storage.storeTokens(
                    authToken = response.data.authToken,
                    refreshToken = response.data.refreshToken
                )
                SignUpResult.Success
            }
            is Error -> SignUpResult.Failure
        }
    }

    suspend fun signInWithGoogle(accessToken: String): SocialSignInResult {
        val request = SocialSignInRequest(
            accessToken = accessToken,
            device = DeviceRequest(
                token = Tasks.await(FirebaseMessaging.getInstance().token),
                platform = Android
            )
        )
        return when (val response = request { api.googleSignIn(request = request) }) {
            is Success -> {
                storage.storeTokens(
                    authToken = response.data.authToken,
                    refreshToken = response.data.refreshToken
                )
                SocialSignInResult.Success
            }
            is Error -> SocialSignInResult.Failure
        }
    }

    suspend fun signInWithFacebook(accessToken: String): SocialSignInResult {
        val request = SocialSignInRequest(
            accessToken = accessToken,
            device = DeviceRequest(
                token = Tasks.await(FirebaseMessaging.getInstance().token),
                platform = Android
            )
        )
        return when (val response = request { api.facebookSignIn(request = request) }) {
            is Success -> {
                storage.storeTokens(
                    authToken = response.data.authToken,
                    refreshToken = response.data.refreshToken
                )
                SocialSignInResult.Success
            }
            is Error -> SocialSignInResult.Failure
        }
    }

    suspend fun signInWithApple(accessToken: String): SocialSignInResult {
        val request = SocialSignInRequest(
            accessToken = accessToken,
            device = DeviceRequest(
                token = Tasks.await(FirebaseMessaging.getInstance().token),
                platform = Android
            )
        )
        return when (val response = request { api.appleSignIn(request = request) }) {
            is Success -> {
                storage.storeTokens(
                    authToken = response.data.authToken,
                    refreshToken = response.data.refreshToken
                )
                SocialSignInResult.Success
            }
            is Error -> SocialSignInResult.Failure
        }
    }

    suspend fun sendResetToken(email: String): SendResetTokenResult {
        return when(request { api.sendResetToken(request = SendResetTokenRequest(email = email)) }) {
            is Success -> SendResetTokenResult.Success
            is Error -> SendResetTokenResult.Failure
        }
    }

    suspend fun verifyResetToken(email: String, token: String): VerifyResetTokenResult {
        val request = VerifyTokenRequest(email = email, resetToken = token)
        return when(request{ api.verifyResetToken(request = request) }) {
            is Success -> VerifyResetTokenResult.Success
            is Error -> VerifyResetTokenResult.Failure
        }
    }

    suspend fun resetPassword(email: String, token: String, password: String): ResetPasswordResult {
        val request =
            ResetPasswordRequest(email = email, resetToken = token, newPassword = password)
        return when (request { api.resetPassword(request = request) }) {
            is Success -> ResetPasswordResult.Success
            is Error -> ResetPasswordResult.Failure
        }
    }

    suspend fun logOut(): LogOutResult {
        val response = request {
            api.logOut(deviceToken = Tasks.await(FirebaseMessaging.getInstance().token))
        }
        return when (response) {
            is Success -> LogOutResult.Success
            is Error -> LogOutResult.Failure
        }
    }
}
