package com.spaceapps.myapplication.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.EmptyCoroutineContext

@Singleton
class AuthDispatcher @Inject constructor() {

    val emitter get() = _emitter.asSharedFlow()
    private val _emitter = MutableSharedFlow<Boolean>(extraBufferCapacity = UNLIMITED)
    private val coroutineScope = CoroutineScope(EmptyCoroutineContext)

    fun requestLogOut() = coroutineScope.launch { _emitter.emit(true) }

    fun requestRestart() = coroutineScope.launch { _emitter.emit(false) }
}
