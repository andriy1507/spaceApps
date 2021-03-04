package com.spaceapps.myapplication.utils

import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import javax.inject.Inject

@ActivityRetainedScoped
class AuthDispatcher @Inject constructor() {

    val emitter = Channel<Boolean>(UNLIMITED)

    fun requestDeauthorization() = emitter.offer(true)
}
