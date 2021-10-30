package com.spaceapps.myapplication.core.repositories.signalr

import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import com.microsoft.signalr.HubConnectionState
import com.spaceapps.myapplication.core.BuildConfig
import com.spaceapps.myapplication.core.local.DataStoreManager
import com.spaceapps.myapplication.core.utils.DispatchersProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.rx2.asSingle
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OptIn(ExperimentalCoroutinesApi::class)
class SignalrRepositoryImpl @Inject constructor(
    private val dispatchersProvider: DispatchersProvider,
    private val dataStoreManager: DataStoreManager
) : SignalrRepository {

    private val hubConnection: HubConnection = HubConnectionBuilder
        .create("${BuildConfig.SERVER_URL}/signalr")
        .withAccessTokenProvider(provideAccessToken())
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

    private fun provideAccessToken() = CoroutineScope(dispatchersProvider.io)
        .async { dataStoreManager.getAccessToken().orEmpty() }
        .asSingle(dispatchersProvider.io)
}
