package com.spaceapps.myapplication.app.network

import com.google.android.gms.tasks.Tasks
import com.google.firebase.messaging.FirebaseMessaging
import com.spaceapps.myapplication.app.AUTH_HEADER
import com.spaceapps.myapplication.app.AUTH_HEADER_PREFIX
import com.spaceapps.myapplication.app.local.DataStoreManager
import com.spaceapps.myapplication.app.models.remote.auth.DeviceRequest
import com.spaceapps.myapplication.app.models.remote.auth.DeviceRequest.Platform.*
import com.spaceapps.myapplication.app.models.remote.auth.RefreshTokenRequest
import com.spaceapps.myapplication.app.network.AuthorizationApi
import com.spaceapps.myapplication.utils.AuthDispatcher
import com.spaceapps.myapplication.utils.Error
import com.spaceapps.myapplication.utils.Success
import com.spaceapps.myapplication.utils.request
import dagger.Lazy
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpaceAppsAuthenticator @Inject constructor(
    private val authApi: Lazy<AuthorizationApi>,
    private val dataStoreManager: DataStoreManager,
    private val authDispatcher: AuthDispatcher
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? = synchronized(this) {
        runBlocking {
            val requestAuthToken = response.request.header(AUTH_HEADER)
            val localAuthToken = dataStoreManager.getAuthToken()
            if (requestAuthToken != localAuthToken) {
                return@runBlocking response.request.newBuilder()
                    .header(AUTH_HEADER, localAuthToken!!)
                    .build()
            }
            val newAuthToken = getAuthToken()
            if (newAuthToken == null) {
                dataStoreManager.removeTokens()
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
        val refreshToken = dataStoreManager.getRefreshToken()
        refreshToken ?: return null
        val request = RefreshTokenRequest(
            refreshToken = refreshToken,
            device = DeviceRequest(
                token = FirebaseMessaging.getInstance().token.await(),
                platform = Android
            )
        )
        return when (val response = request { authApi.get().refreshToken(request = request) }) {
            is Success -> {
                dataStoreManager.storeTokens(
                    authToken = response.data.authToken,
                    refreshToken = response.data.refreshToken
                )
                response.data.authToken
            }
            is Error -> null
        }
    }
}
