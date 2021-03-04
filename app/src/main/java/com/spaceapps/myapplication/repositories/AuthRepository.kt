package com.spaceapps.myapplication.repositories

import com.google.android.gms.tasks.Tasks
import com.google.firebase.messaging.FirebaseMessaging
import com.spaceapps.myapplication.local.AuthTokenStorage
import com.spaceapps.myapplication.models.AuthRequest
import com.spaceapps.myapplication.models.AuthRequest.*
import com.spaceapps.myapplication.models.AuthRequest.Platform.*
import com.spaceapps.myapplication.network.AuthorizationApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val api: AuthorizationApi,
    private val storage: AuthTokenStorage
) {

    suspend fun signIn(email: String, password: String) {
        val request = AuthRequest(
            email = email,
            password = password,
            device = Device(
                token = Tasks.await(FirebaseMessaging.getInstance().token),
                platform = Android
            )
        )
        api.signIn(request = request).also {
            storage.storeTokens(it.authToken, it.refreshToken)
        }
    }

    suspend fun signUp(email: String, password: String) {
        val request = AuthRequest(
            email = email,
            password = password,
            device = Device(
                token = Tasks.await(FirebaseMessaging.getInstance().token),
                platform = Android
            )
        )
        api.signUp(request = request).also {
            storage.storeTokens(it.authToken, it.refreshToken)
        }
    }
}
