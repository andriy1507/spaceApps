package com.spaceapps.myapplication.utils

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthDispatcher @Inject constructor() {

    val emitter = Channel<Boolean>(UNLIMITED)

    fun requestLogOut() = emitter.offer(true)

    fun requestRestart() = emitter.offer(false)
}
