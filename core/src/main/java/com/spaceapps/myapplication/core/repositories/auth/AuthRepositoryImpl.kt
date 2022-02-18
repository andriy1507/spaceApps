package com.spaceapps.myapplication.core.repositories.auth

import com.spaceapps.myapplication.core.local.DataStoreManager
import com.spaceapps.myapplication.core.models.remote.auth.AuthRequest
import com.spaceapps.myapplication.core.models.remote.auth.DeviceRequest
import com.spaceapps.myapplication.core.models.remote.auth.ResetPasswordRequest
import com.spaceapps.myapplication.core.models.remote.auth.SocialSignInRequest
import com.spaceapps.myapplication.core.network.calls.AuthorizationCalls
import com.spaceapps.myapplication.core.repositories.auth.results.*
import com.spaceapps.myapplication.core.utils.*
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository responsible for all authentication logic.
 * @property [calls] Executes network operations.
 * @property [dataStoreManager] Stores authentication data.
 * @property [dispatchersProvider] Provides coroutines dispatchers.
 * @property [deviceInfoProvider] Provides information about device.
 * @constructor Creates instance of repository.
 */
@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val calls: AuthorizationCalls,
    private val dataStoreManager: DataStoreManager,
    private val dispatchersProvider: DispatchersProvider,
    private val deviceInfoProvider: DeviceInfoProvider
) : AuthRepository {

    /**
     * Authenticates already registered user.
     * @param [email] Email used during registration.
     * @param [password] Current user's password.
     * @return Sealed class [SignInResult].
     * On successful authentication [SignInResult.Success] is returned.
     * On failed authentication [SignInResult.Failure] is returned.
     */
    override suspend fun signIn(email: String, password: String): SignInResult =
        withContext(dispatchersProvider.IO) {
            val request = AuthRequest(
                email = email,
                password = password,
                device = provideDeviceModel()
            )
            when (val response = request { calls.signIn(request = request) }) {
                is Success -> {
                    dataStoreManager.storeTokens(response = response.data)
                    SignInResult.Success
                }
                is Error -> SignInResult.Failure
            }
        }

    /**
     * Registers and authenticates non-registered user.
     * @param [email] Email of new user.
     * @param [password] Password of new user.
     * @return Sealed class [SignUpResult].
     * On successful registration and authentication [SignUpResult.Success] is returned.
     * On failed registration or authentication [SignUpResult.Failure] is returned.
     */
    override suspend fun signUp(email: String, password: String): SignUpResult =
        withContext(dispatchersProvider.IO) {
            val request = AuthRequest(
                email = email,
                password = password,
                device = provideDeviceModel()
            )
            when (val response = request { calls.signUp(request = request) }) {
                is Success -> {
                    dataStoreManager.storeTokens(response = response.data)
                    SignUpResult.Success
                }
                is Error -> SignUpResult.Failure
            }
        }

    /**
     * Authenticates registered or non-registered user with Google [accessToken].
     * @param [accessToken] Google access token.
     * @return Sealed class [SocialSignInResult].
     * On successful authentication [SocialSignInResult.Success] is returned.
     * On failed authentication [SocialSignInResult.Failure] is returned.
     */
    override suspend fun signInWithGoogle(accessToken: String): SocialSignInResult =
        withContext(dispatchersProvider.IO) {
            val request = SocialSignInRequest(
                accessToken = accessToken,
                device = provideDeviceModel()
            )
            when (
                val response =
                    request { calls.googleSignIn(request = request) }
            ) {
                is Success -> {
                    dataStoreManager.storeTokens(response = response.data)
                    SocialSignInResult.Success
                }
                is Error -> SocialSignInResult.Failure
            }
        }

    /**
     * Authenticates registered or non-registered user with Facebook [accessToken].
     * @param [accessToken] Facebook access token.
     * @return Sealed class [SocialSignInResult].
     * On successful authentication [SocialSignInResult.Success] is returned.
     * On failed authentication [SocialSignInResult.Failure] is returned.
     */
    override suspend fun signInWithFacebook(accessToken: String): SocialSignInResult =
        withContext(dispatchersProvider.IO) {
            val request = SocialSignInRequest(
                accessToken = accessToken,
                device = provideDeviceModel()
            )
            when (
                val response =
                    request { calls.facebookSignIn(request = request) }
            ) {
                is Success -> {
                    dataStoreManager.storeTokens(response = response.data)
                    SocialSignInResult.Success
                }
                is Error -> SocialSignInResult.Failure
            }
        }

    /**
     * Authenticates registered or non-registered user with Apple [accessToken].
     * @param [accessToken] Apple access token.
     * @return Sealed class [SocialSignInResult].
     * On successful authentication [SocialSignInResult.Success] is returned.
     * On failed authentication [SocialSignInResult.Failure] is returned.
     */
    override suspend fun signInWithApple(accessToken: String): SocialSignInResult =
        withContext(dispatchersProvider.IO) {
            val request = SocialSignInRequest(
                accessToken = accessToken,
                device = provideDeviceModel()
            )
            when (val response = request { calls.appleSignIn(request = request) }) {
                is Success -> {
                    dataStoreManager.storeTokens(response = response.data)
                    SocialSignInResult.Success
                }
                is Error -> SocialSignInResult.Failure
            }
        }

    override suspend fun sendResetCode(email: String): SendResetCodeResult =
        withContext(dispatchersProvider.IO) {
            when (request { calls.sendResetCode(email = email) }) {
                is Success -> SendResetCodeResult.Success
                is Error -> SendResetCodeResult.Failure
            }
        }

    override suspend fun verifyResetCode(email: String, code: String): VerifyResetCodeResult =
        withContext(dispatchersProvider.IO) {
            when (request { calls.verifyResetCode(email = email, resetCode = code) }) {
                is Success -> VerifyResetCodeResult.Success
                is Error -> VerifyResetCodeResult.Failure
            }
        }

    override suspend fun resetPassword(
        email: String,
        code: String,
        password: String
    ): ResetPasswordResult = withContext(dispatchersProvider.IO) {
        val request =
            ResetPasswordRequest(email = email, resetCode = code, newPassword = password)
        when (request { calls.resetPassword(request = request) }) {
            is Success -> ResetPasswordResult.Success
            is Error -> ResetPasswordResult.Failure
        }
    }

    override suspend fun logOut(): LogOutResult = withContext(dispatchersProvider.IO) {
        val response = request {
            calls.logOut(installationId = deviceInfoProvider.getFirebaseInstallationId())
        }
        when (response) {
            is Success -> LogOutResult.Success
            is Error -> LogOutResult.Failure
        }
    }

    override suspend fun addDevice(token: String): AddDeviceResult {
        val device = provideDeviceModel().copy(token = token)
        return when (request { calls.addDevice(device = device) }) {
            is Success -> AddDeviceResult.Success
            is Error -> AddDeviceResult.Failure
        }
    }

    private suspend fun provideDeviceModel() = DeviceRequest(
        token = deviceInfoProvider.getFirebaseMessagingToken(),
        installationId = deviceInfoProvider.getFirebaseInstallationId(),
        manufacturer = deviceInfoProvider.provideManufacturer(),
        model = deviceInfoProvider.provideModel(),
        osVersion = deviceInfoProvider.provideOsVersion()
    )
}
