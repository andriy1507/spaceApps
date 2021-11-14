package com.spaceapps.myapplication.core.network

import com.spaceapps.myapplication.core.AUTH_HEADER
import com.spaceapps.myapplication.core.AUTH_HEADER_PREFIX
import com.spaceapps.myapplication.core.local.DataStoreManager
import com.spaceapps.myapplication.core.models.remote.profile.Platform.*
import com.spaceapps.myapplication.core.network.calls.AuthorizationCalls
import com.spaceapps.myapplication.core.utils.AuthDispatcher
import com.spaceapps.myapplication.core.utils.Error
import com.spaceapps.myapplication.core.utils.Success
import com.spaceapps.myapplication.core.utils.request
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
    private val authCalls: Lazy<AuthorizationCalls>,
    private val dataStoreManager: DataStoreManager,
    private val authDispatcher: AuthDispatcher
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? = synchronized(this) {
        runBlocking {
            val requestAuthToken = response.request.header(AUTH_HEADER)
            val localAuthToken = dataStoreManager.getAccessToken()
            if (requestAuthToken != localAuthToken) {
                return@runBlocking response.request.newBuilder()
                    .header(AUTH_HEADER, localAuthToken!!)
                    .build()
            }
            val newAuthToken = getAccessToken()
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

    private suspend fun getAccessToken(): String? {
        val refreshToken = dataStoreManager.getRefreshToken()
        refreshToken ?: return null
        return when (val response = request { authCalls.get().refreshToken(refreshToken) }) {
            is Success -> {
                dataStoreManager.storeTokens(response = response.data)
                response.data.accessToken
            }
            is Error -> null
        }
    }
}
