package com.spaceapps.navigation

import androidx.navigation.NavHostController
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.EmptyCoroutineContext

typealias NavCommand = (NavHostController) -> Unit

@ActivityRetainedScoped
/**
 * [Navigator] emits [NavCommand] to [emitter]
 * which are consumed by [NavHostController].
 * */
class Navigator @Inject constructor() {

    private val coroutineScope = CoroutineScope(EmptyCoroutineContext)
    private val _emitter = MutableSharedFlow<NavCommand>(extraBufferCapacity = Channel.UNLIMITED)
    val emitter get() = _emitter.asSharedFlow()

    fun emit(command: NavCommand) = coroutineScope.launch { _emitter.emit(command) }
}
