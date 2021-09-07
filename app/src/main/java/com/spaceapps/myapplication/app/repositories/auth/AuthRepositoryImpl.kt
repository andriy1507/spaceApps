package com.spaceapps.myapplication.app.repositories.auth

import com.google.firebase.messaging.FirebaseMessaging
import com.spaceapps.myapplication.app.local.DataStoreManager
import com.spaceapps.myapplication.app.models.remote.auth.*
import com.spaceapps.myapplication.app.models.remote.auth.DeviceRequest.Platform.Android
import com.spaceapps.myapplication.app.network.AuthorizationApi
import com.spaceapps.myapplication.app.repositories.auth.results.*
import com.spaceapps.myapplication.utils.Error
import com.spaceapps.myapplication.utils.Success
import com.spaceapps.myapplication.utils.request
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val api: AuthorizationApi,
    private val dataStoreManager: DataStoreManager
) : AuthRepository {

    override suspend fun signIn(email: String, password: String): SignInResult {
        val request = AuthRequest(
            email = email,
            password = password,
            device = DeviceRequest(
                token = FirebaseMessaging.getInstance().token.await(),
                platform = Android
            )
        )
        return when (val response = request { api.signIn(request = request) }) {
            is Success -> {
                dataStoreManager.storeTokens(
                    authToken = response.data.authToken,
                    refreshToken = response.data.refreshToken
                )
                SignInResult.Success
            }
            is Error -> SignInResult.Failure
        }
    }

    override suspend fun signUp(email: String, password: String): SignUpResult {
        val request = AuthRequest(
            email = email,
            password = password,
            device = DeviceRequest(
                token = FirebaseMessaging.getInstance().token.await(),
                platform = Android
            )
        )
        return when (val response = request { api.signUp(request = request) }) {
            is Success -> {
                dataStoreManager.storeTokens(
                    authToken = response.data.authToken,
                    refreshToken = response.data.refreshToken
                )
                SignUpResult.Success
            }
            is Error -> SignUpResult.Failure
        }
    }

    override suspend fun signInWithGoogle(accessToken: String): SocialSignInResult {
        val request = SocialSignInRequest(
            accessToken = accessToken,
            device = DeviceRequest(
                token = FirebaseMessaging.getInstance().token.await(),
                platform = Android
            )
        )
        return when (val response = request { api.googleSignIn(request = request) }) {
            is Success -> {
                dataStoreManager.storeTokens(
                    authToken = response.data.authToken,
                    refreshToken = response.data.refreshToken
                )
                SocialSignInResult.Success
            }
            is Error -> SocialSignInResult.Failure
        }
    }

    override suspend fun signInWithFacebook(accessToken: String): SocialSignInResult {
        val request = SocialSignInRequest(
            accessToken = accessToken,
            device = DeviceRequest(
                token = FirebaseMessaging.getInstance().token.await(),
                platform = Android
            )
        )
        return when (val response = request { api.facebookSignIn(request = request) }) {
            is Success -> {
                dataStoreManager.storeTokens(
                    authToken = response.data.authToken,
                    refreshToken = response.data.refreshToken
                )
                SocialSignInResult.Success
            }
            is Error -> SocialSignInResult.Failure
        }
    }

    override suspend fun signInWithApple(accessToken: String): SocialSignInResult {
        val request = SocialSignInRequest(
            accessToken = accessToken,
            device = DeviceRequest(
                token = FirebaseMessaging.getInstance().token.await(),
                platform = Android
            )
        )
        return when (val response = request { api.appleSignIn(request = request) }) {
            is Success -> {
                dataStoreManager.storeTokens(
                    authToken = response.data.authToken,
                    refreshToken = response.data.refreshToken
                )
                SocialSignInResult.Success
            }
            is Error -> SocialSignInResult.Failure
        }
    }

    override suspend fun sendResetToken(email: String): SendResetTokenResult {
        return when (request { api.sendResetToken(request = SendResetTokenRequest(email = email)) }) {
            is Success -> SendResetTokenResult.Success
            is Error -> SendResetTokenResult.Failure
        }
    }

    override suspend fun verifyResetToken(email: String, token: String): VerifyResetTokenResult {
        val request = VerifyTokenRequest(email = email, resetToken = token)
        return when (request { api.verifyResetToken(request = request) }) {
            is Success -> VerifyResetTokenResult.Success
            is Error -> VerifyResetTokenResult.Failure
        }
    }

    override suspend fun resetPassword(email: String, token: String, password: String): ResetPasswordResult {
        val request =
            ResetPasswordRequest(email = email, resetToken = token, newPassword = password)
        return when (request { api.resetPassword(request = request) }) {
            is Success -> ResetPasswordResult.Success
            is Error -> ResetPasswordResult.Failure
        }
    }

    override suspend fun logOut(): LogOutResult {
        val response = request {
            api.logOut(deviceToken = FirebaseMessaging.getInstance().token.await())
        }
        return when (response) {
            is Success -> LogOutResult.Success
            is Error -> LogOutResult.Failure
        }
    }
}
