package com.spaceapps.myapplication.features.profile

import androidx.lifecycle.ViewModel
import com.spaceapps.navigation.Screens.*
import com.spaceapps.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val navigator: Navigator,
) : ViewModel() {

    private val _events = MutableSharedFlow<ProfileEvent>()
    val events: SharedFlow<ProfileEvent>
        get() = _events.asSharedFlow()

    fun goDevices() = navigator.emit { it.navigate(Devices.route) }

    fun goNotifications() =
        navigator.emit { it.navigate(Notifications.route) }

    fun goPlayer() = navigator.emit { it.navigate(Player.route) }
}
