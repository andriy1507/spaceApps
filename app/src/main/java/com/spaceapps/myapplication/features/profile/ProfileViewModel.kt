package com.spaceapps.myapplication.features.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.app.ProfileGraph
import com.spaceapps.myapplication.app.network.calls.ProfileCalls
import com.spaceapps.myapplication.utils.Error
import com.spaceapps.myapplication.utils.NavigationDispatcher
import com.spaceapps.myapplication.utils.Success
import com.spaceapps.myapplication.utils.request
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val navigationDispatcher: NavigationDispatcher,
    private val calls: ProfileCalls
) : ViewModel() {

    init {
        getProfile()
    }

    private val _events = MutableSharedFlow<ProfileEvents>()
    val events: SharedFlow<ProfileEvents>
        get() = _events.asSharedFlow()

    fun goDevices() = navigationDispatcher.emit { it.navigate(ProfileGraph.Devices.route) }

    fun goNotifications() =
        navigationDispatcher.emit { it.navigate(ProfileGraph.Notifications.route) }

    private fun getProfile() = viewModelScope.launch {
        when (val response = request { calls.getProfile() }) {
            is Success -> {
            }
            is Error -> _events.emit(ProfileEvents.ShowSnackBar(R.string.unexpected_error))
        }
    }
}
