package com.spaceapps.myapplication.network

import com.spaceapps.myapplication.local.AuthTokenStorage
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
    private val authTokenStorage: AuthTokenStorage
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val authToken = getAuthToken()
        return if (authToken == null) {
            authTokenStorage.removeTokens()
            null
        } else {
            response.request.newBuilder()
                .header(AUTH_HEADER, AUTH_HEADER_PREFIX + authToken)
                .build()
        }
    }

    private fun getAuthToken(): String? = runBlocking {
        request { authApi.get().refreshToken(authTokenStorage.refreshToken.orEmpty()) }
            .onSuccess {
                authTokenStorage.storeTokens(it.authToken, it.refreshToken)
                return@runBlocking it.authToken
            }
        return@runBlocking null
    }
}