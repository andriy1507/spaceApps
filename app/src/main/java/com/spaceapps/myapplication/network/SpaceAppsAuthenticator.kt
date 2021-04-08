package com.spaceapps.myapplication.network

import com.spaceapps.myapplication.AUTH_HEADER
import com.spaceapps.myapplication.AUTH_HEADER_PREFIX
import com.spaceapps.myapplication.local.AuthTokenStorage
import com.spaceapps.myapplication.models.remote.auth.RefreshTokenRequest
import com.spaceapps.myapplication.utils.AuthDispatcher
import com.spaceapps.myapplication.utils.request
import dagger.Lazy
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

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
                authDispatcher.requestDeauthorization()
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
        request {
            authApi.get().refreshToken(request = RefreshTokenRequest(refreshToken = refreshToken))
        }.onSuccess {
            authTokenStorage.storeTokens(
                authToken = it.authToken,
                refreshToken = it.refreshToken
            )
            return it.authToken
        }
        return null
    }
}
