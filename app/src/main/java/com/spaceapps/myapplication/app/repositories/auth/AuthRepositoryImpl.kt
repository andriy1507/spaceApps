package com.spaceapps.myapplication.app.repositories.auth

import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import com.spaceapps.myapplication.app.local.DataStoreManager
import com.spaceapps.myapplication.app.models.remote.auth.*
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
                device = provideDeviceModel()
            )
            when (val response = request { calls.signIn(request = request) }) {
                is Success -> {
                    storeResponse(response = response.data)
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
                device = provideDeviceModel()
            )
            when (val response = request { calls.signUp(request = request) }) {
                is Success -> {
                    storeResponse(response = response.data)
                    SignUpResult.Success
                }
                is Error -> SignUpResult.Failure
            }
        }

    override suspend fun signInWithGoogle(accessToken: String): SocialSignInResult =
        withContext(dispatchersProvider.io) {
            val request = SocialSignInRequest(
                accessToken = accessToken,
                device = provideDeviceModel()
            )
            when (
                val response =
                    request { calls.googleSignIn(request = request) }
            ) {
                is Success -> {
                    storeResponse(response = response.data)
                    SocialSignInResult.Success
                }
                is Error -> SocialSignInResult.Failure
            }
        }

    override suspend fun signInWithFacebook(accessToken: String): SocialSignInResult =
        withContext(dispatchersProvider.io) {
            val request = SocialSignInRequest(
                accessToken = accessToken,
                device = provideDeviceModel()
            )
            when (
                val response =
                    request { calls.facebookSignIn(request = request) }
            ) {
                is Success -> {
                    storeResponse(response = response.data)
                    SocialSignInResult.Success
                }
                is Error -> SocialSignInResult.Failure
            }
        }

    override suspend fun signInWithApple(accessToken: String): SocialSignInResult =
        withContext(dispatchersProvider.io) {
            val request = SocialSignInRequest(
                accessToken = accessToken,
                device = provideDeviceModel()
            )
            when (val response = request { calls.appleSignIn(request = request) }) {
                is Success -> {
                    storeResponse(response = response.data)
                    SocialSignInResult.Success
                }
                is Error -> SocialSignInResult.Failure
            }
        }

    override suspend fun sendResetCode(email: String): SendResetCodeResult =
        withContext(dispatchersProvider.io) {
            when (request { calls.sendResetCode(request = SendResetCodeRequest(email = email)) }) {
                is Success -> SendResetCodeResult.Success
                is Error -> SendResetCodeResult.Failure
            }
        }

    override suspend fun verifyResetCode(email: String, code: String): VerifyResetCodeResult =
        withContext(dispatchersProvider.io) {
            val request = VerifyCodeRequest(email = email, resetCode = code)
            when (request { calls.verifyResetCode(request = request) }) {
                is Success -> VerifyResetCodeResult.Success
                is Error -> VerifyResetCodeResult.Failure
            }
        }

    override suspend fun resetPassword(
        email: String,
        code: String,
        password: String
    ): ResetPasswordResult = withContext(dispatchersProvider.io) {
        val request =
            ResetPasswordRequest(email = email, resetCode = code, newPassword = password)
        when (request { calls.resetPassword(request = request) }) {
            is Success -> ResetPasswordResult.Success
            is Error -> ResetPasswordResult.Failure
        }
    }

    override suspend fun logOut(): LogOutResult = withContext(dispatchersProvider.io) {
        val response = request {
            calls.logOut(installationId = FirebaseInstallations.getInstance().id.await())
        }
        when (response) {
            is Success -> LogOutResult.Success
            is Error -> LogOutResult.Failure
        }
    }

    private suspend fun storeResponse(response: AuthTokenResponse) {
        dataStoreManager.storeTokens(
            accessToken = response.accessToken,
            refreshToken = response.refreshToken
        )
    }

    private suspend fun provideDeviceModel() = DeviceRequest(
        token = FirebaseMessaging.getInstance().token.await(),
        installationId = FirebaseInstallations.getInstance().id.await()
    )
}
