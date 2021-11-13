package com.spaceapps.myapplication.core.utils

import kotlinx.coroutines.CoroutineDispatcher

@Suppress("PropertyName")
interface DispatchersProvider {
    val Main: CoroutineDispatcher
    val IO: CoroutineDispatcher
    val Default: CoroutineDispatcher
    val Unconfined: CoroutineDispatcher
}
