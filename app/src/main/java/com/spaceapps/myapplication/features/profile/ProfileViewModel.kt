package com.spaceapps.myapplication.features.profile

import androidx.lifecycle.ViewModel
import com.spaceapps.myapplication.app.Screens.*
import com.spaceapps.myapplication.utils.NavigationDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val navigationDispatcher: NavigationDispatcher,
) : ViewModel() {

    private val _events = MutableSharedFlow<ProfileEvents>()
    val events: SharedFlow<ProfileEvents>
        get() = _events.asSharedFlow()

    fun goDevices() = navigationDispatcher.emit { it.navigate(Devices.route) }

    fun goNotifications() =
        navigationDispatcher.emit { it.navigate(Notifications.route) }
}
