package com.spaceapps.myapplication.app.repositories.signalr

import com.microsoft.signalr.*
import com.spaceapps.myapplication.BuildConfig
import com.spaceapps.myapplication.app.network.AuthInterceptor
import com.spaceapps.myapplication.app.network.SpaceAppsAuthenticator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OptIn(ExperimentalCoroutinesApi::class)
class SignalrRepositoryImpl @Inject constructor(
    logger: HttpLoggingInterceptor,
    authInterceptor: AuthInterceptor,
    authenticator: SpaceAppsAuthenticator
) : SignalrRepository {

    private val hubConnection: HubConnection = HubConnectionBuilder
        .create("${BuildConfig.SERVER_URL}/signalr")
        .setHttpClientBuilderCallback {
            it.addInterceptor(logger)
            it.addInterceptor(authInterceptor)
            it.authenticator(authenticator)
        }
        .build()

    override fun subscribeTest() = callbackFlow<String> {
        try {
            hubConnection.checkConnection()
            hubConnection.on("test", { trySend(it) }, String::class.java)
            hubConnection.send("test")
        } catch (e: Exception) {
            Timber.e(e)
        } finally {
            awaitClose { close() }
        }
    }

    private fun HubConnection.checkConnection() {
        if (connectionState != HubConnectionState.CONNECTED) start().blockingAwait()
    }
}
