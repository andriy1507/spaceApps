package com.spaceapps.myapplication.utils

import androidx.navigation.NavController
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject
import javax.inject.Singleton

typealias NavEvent = NavController.() -> Unit

@Singleton
class NavDispatcher @Inject constructor() {

    fun emit(event: NavEvent) = events.offer(event)

    val events = Channel<NavEvent>()
}
