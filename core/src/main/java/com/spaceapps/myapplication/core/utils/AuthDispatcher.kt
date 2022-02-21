package com.spaceapps.myapplication.core.utils

import kotlinx.coroutines.flow.SharedFlow

interface AuthDispatcher {

    val emitter: SharedFlow<Boolean>

    fun requestLogOut()

    fun requestRestart()
}
