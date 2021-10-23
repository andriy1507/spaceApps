package com.spaceapps.myapplication.app.repositories.signalr

import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import com.microsoft.signalr.HubConnectionState
import com.spaceapps.myapplication.BuildConfig
import com.spaceapps.myapplication.app.local.DataStoreManager
import com.spaceapps.myapplication.app.models.remote.auth.RefreshTokenRequest
import com.spaceapps.myapplication.app.network.calls.AuthorizationCalls
import com.spaceapps.myapplication.utils.tryOrNull
import io.reactivex.Single
import io.reactivex.SingleObserver
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OptIn(ExperimentalCoroutinesApi::class)
class SignalrRepositoryImpl @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val calls: AuthorizationCalls
) : SignalrRepository {

    private val accessTokenProvider = object : Single<String>() {
        override fun subscribeActual(observer: SingleObserver<in String>) {
            if (runBlocking { dataStoreManager.getAccessTokenExpired() }.isBefore(LocalDateTime.now())) {
                observer.onSuccess(
                    runBlocking {
                        val response =
                            calls.refreshToken(RefreshTokenRequest(dataStoreManager.getRefreshToken()!!))
                        dataStoreManager.storeTokens(response.accessToken, response.refreshToken)
                        response.accessToken
                    }
                )
            } else {
                observer.onSuccess(runBlocking { dataStoreManager.getAccessToken().orEmpty() })
            }
        }
    }

    private val hubConnection: HubConnection = HubConnectionBuilder
        .create("${BuildConfig.SERVER_URL}signalr")
        .withAccessTokenProvider(accessTokenProvider)
        .build()

    fun subscribeTest() = callbackFlow<String> {
        tryOrNull {
            hubConnection.checkConnection()
            hubConnection.on(
                "test",
                { trySend(it) },
                String::class.java
            )
        }
        hubConnection.send("subscribeTest")
    }

    private fun HubConnection.checkConnection() {
        if (connectionState != HubConnectionState.CONNECTED) start().blockingAwait()
    }
}
