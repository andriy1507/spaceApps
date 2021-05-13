package com.spaceapps.myapplication.network

import com.google.android.gms.tasks.Tasks
import com.google.firebase.messaging.FirebaseMessaging
import com.spaceapps.myapplication.app.AUTH_HEADER
import com.spaceapps.myapplication.app.AUTH_HEADER_PREFIX
import com.spaceapps.myapplication.local.AuthTokenStorage
import com.spaceapps.myapplication.models.remote.auth.DeviceRequest
import com.spaceapps.myapplication.models.remote.auth.DeviceRequest.Platform.*
import com.spaceapps.myapplication.models.remote.auth.RefreshTokenRequest
import com.spaceapps.myapplication.utils.AuthDispatcher
import com.spaceapps.myapplication.utils.Error
import com.spaceapps.myapplication.utils.Success
import com.spaceapps.myapplication.utils.request
import dagger.Lazy
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpaceAppsAuthenticator @Inject constructor(
    private val authApi: Lazy<AuthorizationApi>,
    private val authTokenStorage: AuthTokenStorage,
    private val authDispatcher: AuthDispatcher
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? = synchronized(this) {
        runBlocking {
            val requestAuthToken = response.request.header(AUTH_HEADER)
            val localAuthToken = authTokenStorage.getAuthToken()
            if (requestAuthToken != localAuthToken) {
                return@runBlocking response.request.newBuilder()
                    .header(AUTH_HEADER, localAuthToken!!)
                    .build()
            }
            val newAuthToken = getAuthToken()
            if (newAuthToken == null) {
                authTokenStorage.clear()
                authDispatcher.requestLogOut()
                null
            } else {
                response.request.newBuilder()
                    .header(AUTH_HEADER, "$AUTH_HEADER_PREFIX $newAuthToken")
                    .build()
            }
        }
    }

    private suspend fun getAuthToken(): String? {
        val refreshToken = authTokenStorage.getRefreshToken()
        refreshToken ?: return null
        val request = RefreshTokenRequest(
            refreshToken = refreshToken,
            device = DeviceRequest(
                token = Tasks.await(FirebaseMessaging.getInstance().token),
                platform = Android
            )
        )
        return when (val response = request { authApi.get().refreshToken(request = request) }) {
            is Success -> {
                authTokenStorage.storeTokens(
                    authToken = response.data.authToken,
                    refreshToken = response.data.refreshToken
                )
                response.data.authToken
            }
            is Error -> null
        }
    }
}
