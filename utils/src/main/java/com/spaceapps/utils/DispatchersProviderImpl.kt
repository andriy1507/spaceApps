package com.spaceapps.utils

import com.spaceapps.myapplication.core.utils.DispatchersProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DispatchersProviderImpl : DispatchersProvider {
    override val Main: CoroutineDispatcher get() = Dispatchers.Main
    override val IO: CoroutineDispatcher get() = Dispatchers.IO
    override val Default: CoroutineDispatcher get() = Dispatchers.Default
    override val Unconfined: CoroutineDispatcher get() = Dispatchers.Unconfined
}
