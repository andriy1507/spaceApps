package com.spaceapps.myapplication.app.repositories.auth

import com.google.firebase.messaging.FirebaseMessaging
import com.spaceapps.myapplication.app.local.DataStoreManager
import com.spaceapps.myapplication.app.models.remote.auth.*
import com.spaceapps.myapplication.app.models.remote.auth.DeviceRequest.Platform.Android
import com.spaceapps.myapplication.app.network.calls.AuthorizationCalls
import com.spaceapps.myapplication.app.repositories.auth.results.*
import com.spaceapps.myapplication.utils.DispatchersProvider
import com.spaceapps.myapplication.utils.Error
import com.spaceapps.myapplication.utils.Success
import com.spaceapps.myapplication.utils.request
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val calls: AuthorizationCalls,
    private val dataStoreManager: DataStoreManager,
    private val dispatchersProvider: DispatchersProvider
) : AuthRepository {

    override suspend fun signIn(email: String, password: String): SignInResult =
        withContext(dispatchersProvider.io) {
            val request = AuthRequest(
                email = email,
                password = password,
                device = DeviceRequest(
                    token = FirebaseMessaging.getInstance().token.await(),
                    platform = Android
                )
            )
            when (val response = request { calls.signIn(request = request) }) {
                is Success -> {
                    dataStoreManager.storeTokens(
                        accessToken = response.data.accessToken,
                        refreshToken = response.data.refreshToken
                    )
                    SignInResult.Success
                }
                is Error -> SignInResult.Failure
            }
        }

    override suspend fun signUp(email: String, password: String): SignUpResult =
        withContext(dispatchersProvider.io) {
            val request = AuthRequest(
                email = email,
                password = password,
                device = DeviceRequest(
                    token = FirebaseMessaging.getInstance().token.await(),
                    platform = Android
                )
            )
            when (val response = request { calls.signUp(request = request) }) {
                is Success -> {
                    dataStoreManager.storeTokens(
                        accessToken = response.data.accessToken,
                        refreshToken = response.data.refreshToken
                    )
                    SignUpResult.Success
                }
                is Error -> SignUpResult.Failure
            }
        }

    override suspend fun signInWithGoogle(accessToken: String): SocialSignInResult =
        withContext(dispatchersProvider.io) {
            val request = SocialSignInRequest(
                accessToken = accessToken,
                device = DeviceRequest(
                    token = FirebaseMessaging.getInstance().token.await(),
                    platform = Android
                )
            )
            when (
                val response =
                    request { calls.googleSignIn(request = request) }
            ) {
                is Success -> {
                    dataStoreManager.storeTokens(
                        accessToken = response.data.accessToken,
                        refreshToken = response.data.refreshToken
                    )
                    SocialSignInResult.Success
                }
                is Error -> SocialSignInResult.Failure
            }
        }

    override suspend fun signInWithFacebook(accessToken: String): SocialSignInResult =
        withContext(dispatchersProvider.io) {
            val request = SocialSignInRequest(
                accessToken = accessToken,
                device = DeviceRequest(
                    token = FirebaseMessaging.getInstance().token.await(),
                    platform = Android
                )
            )
            when (
                val response =
                    request { calls.facebookSignIn(request = request) }
            ) {
                is Success -> {
                    dataStoreManager.storeTokens(
                        accessToken = response.data.accessToken,
                        refreshToken = response.data.refreshToken
                    )
                    SocialSignInResult.Success
                }
                is Error -> SocialSignInResult.Failure
            }
        }

    override suspend fun signInWithApple(accessToken: String): SocialSignInResult =
        withContext(dispatchersProvider.io) {
            val request = SocialSignInRequest(
                accessToken = accessToken,
                device = DeviceRequest(
                    token = FirebaseMessaging.getInstance().token.await(),
                    platform = Android
                )
            )
            when (
                val response =
                    request { calls.appleSignIn(request = request) }
            ) {
                is Success -> {
                    dataStoreManager.storeTokens(
                        accessToken = response.data.accessToken,
                        refreshToken = response.data.refreshToken
                    )
                    SocialSignInResult.Success
                }
                is Error -> SocialSignInResult.Failure
            }
        }

    override suspend fun sendResetToken(email: String): SendResetTokenResult =
        withContext(dispatchersProvider.io) {
            when (
                request {
                    calls.sendResetToken(
                        request = SendResetTokenRequest(
                            email = email
                        )
                    )
                }
            ) {
                is Success -> SendResetTokenResult.Success
                is Error -> SendResetTokenResult.Failure
            }
        }

    override suspend fun verifyResetToken(email: String, token: String): VerifyResetTokenResult =
        withContext(dispatchersProvider.io) {
            val request = VerifyTokenRequest(email = email, resetToken = token)
            when (request { calls.verifyResetToken(request = request) }) {
                is Success -> VerifyResetTokenResult.Success
                is Error -> VerifyResetTokenResult.Failure
            }
        }

    override suspend fun resetPassword(
        email: String,
        token: String,
        password: String
    ): ResetPasswordResult = withContext(dispatchersProvider.io) {
        val request =
            ResetPasswordRequest(email = email, resetToken = token, newPassword = password)
        when (request { calls.resetPassword(request = request) }) {
            is Success -> ResetPasswordResult.Success
            is Error -> ResetPasswordResult.Failure
        }
    }

    override suspend fun logOut(): LogOutResult = withContext(dispatchersProvider.io) {
        val response = request {
            calls.logOut(deviceToken = FirebaseMessaging.getInstance().token.await())
        }
        when (response) {
            is Success -> LogOutResult.Success
            is Error -> LogOutResult.Failure
        }
    }
}
