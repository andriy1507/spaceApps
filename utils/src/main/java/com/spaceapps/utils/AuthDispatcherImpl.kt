package com.spaceapps.utils

import com.spaceapps.myapplication.core.utils.AuthDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

class AuthDispatcherImpl : AuthDispatcher {

    override val emitter get() = _emitter.asSharedFlow()

    private val _emitter = MutableSharedFlow<Boolean>(extraBufferCapacity = UNLIMITED)
    private val coroutineScope = CoroutineScope(EmptyCoroutineContext)

    override fun requestLogOut() {
        coroutineScope.launch { _emitter.emit(true) }
    }

    override fun requestRestart() {
        coroutineScope.launch { _emitter.emit(false) }
    }
}
