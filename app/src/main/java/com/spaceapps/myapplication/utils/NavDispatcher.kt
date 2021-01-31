package com.spaceapps.myapplication.utils

import androidx.navigation.NavController
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject

typealias NavEvent = NavController.() -> Unit

class NavDispatcher @Inject constructor() {

    fun emit(event: NavEvent) = events.offer(event)

    companion object {
        val events = Channel<NavEvent>()
    }
}
