package com.spaceapps.myapplication.repositories

import com.spaceapps.myapplication.local.AuthTokenStorage
import com.spaceapps.myapplication.network.AuthorizationApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val api: AuthorizationApi,
    private val storage: AuthTokenStorage
) {

    suspend fun signIn(email: String, password: String) {
        api.signIn(email, password).also {
            storage.storeTokens(it.authToken, it.refreshToken)
        }
        storage.getFcmToken()?.let { api.sendFcmToken(it) }
    }

    suspend fun signUp(email: String, password: String) {
        api.signUp(email, password).also {
            storage.storeTokens(it.authToken, it.refreshToken)
        }
        storage.getFcmToken()?.let { api.sendFcmToken(it) }
    }
}
