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

    suspend fun login(email: String, password: String) {
        api.login(email, password).also {
            storage.storeTokens(it.authToken, it.refreshToken)
        }
        storage.fcmToken?.let { api.sendFcmToken(it) }
    }

    suspend fun register(email: String, password: String) {
        api.register(email, password).also {
            storage.storeTokens(it.authToken, it.refreshToken)
        }
        storage.fcmToken?.let { api.sendFcmToken(it) }
    }
}