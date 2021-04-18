package com.spaceapps.myapplication.features.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spaceapps.myapplication.AUTH_HEADER
import com.spaceapps.myapplication.SERVER_CHAT_SOCKET_URL
import com.spaceapps.myapplication.local.AuthTokenStorage
import com.spaceapps.myapplication.models.remote.chat.MessageResponse
import com.spaceapps.myapplication.network.ChatApi
import com.spaceapps.myapplication.utils.launch
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import okhttp3.*
import timber.log.Timber
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val authTokenStorage: AuthTokenStorage,
    private val okHttpClient: OkHttpClient,
    private val moshi: Moshi,
    private val chatApi: ChatApi,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var webSocket: WebSocket? = null
    private val receivedMessages = MutableSharedFlow<MessageResponse>()
    private val sentMessages = MutableSharedFlow<MessageResponse>()
    private val conversationId = savedStateHandle.get<String>("conversationId").orEmpty()

    init {
        launch { connectToSocket() }
    }

    private suspend fun connectToSocket() {
        val request = Request.Builder().apply {
            authTokenStorage.getAuthToken()?.let { token -> addHeader(AUTH_HEADER, token) }
            addHeader("ConversationId", conversationId)
        }.url(SERVER_CHAT_SOCKET_URL).build()
        webSocket = okHttpClient.newWebSocket(
            request,
            object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    val token = runBlocking { authTokenStorage.getAuthToken() }
                    webSocket.send(token ?: return)
                    collectSentMessages()
                }

                override fun onMessage(webSocket: WebSocket, text: String) = runBlocking {
                    Timber.d(text)
                    val message = moshi.adapter(MessageResponse::class.java).fromJson(text)
                    message ?: return@runBlocking
                    receivedMessages.emit(message)
                }

                override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                    val token = runBlocking { authTokenStorage.getAuthToken() }
                    webSocket.send(token ?: return)
                }
            }
        )
    }

    private fun collectSentMessages() {
        sentMessages.onEach {
            webSocket?.send(moshi.adapter(MessageResponse::class.java).toJson(it))
        }.launchIn(viewModelScope)
    }

    private fun sendMessage(text: String) = launch {
        sentMessages.emit(
            MessageResponse(
                messageId = "",
                conversationId = conversationId,
                messageText = text,
                dateTime = LocalDateTime.now()
            )
        )
    }
}
