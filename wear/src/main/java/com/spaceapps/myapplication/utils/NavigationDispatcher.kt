package com.spaceapps.myapplication.utils

import androidx.navigation.NavHostController
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.EmptyCoroutineContext

typealias NavigationCommand = (NavHostController) -> Unit

@ActivityRetainedScoped
class NavigationDispatcher @Inject constructor() {

    private val coroutineScope = CoroutineScope(EmptyCoroutineContext)
    private val _emitter = MutableSharedFlow<NavigationCommand>(extraBufferCapacity = UNLIMITED)
    val emitter get() = _emitter.asSharedFlow()

    fun emit(command: NavigationCommand) = coroutineScope.launch { _emitter.emit(command) }
}
