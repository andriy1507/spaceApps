package com.spaceapps.myapplication.utils

import kotlin.coroutines.CoroutineContext

interface DispatchersProvider {
    val main: CoroutineContext
    val io: CoroutineContext
    val default: CoroutineContext
}
