package com.spaceapps.myapplication.features.devices

import androidx.lifecycle.ViewModel
import com.spaceapps.myapplication.app.repositories.devices.DevicesRepository
import com.spaceapps.myapplication.utils.NavigationDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class DevicesViewModel @Inject constructor(
    private val repository: DevicesRepository,
    private val navigationDispatcher: NavigationDispatcher
) : ViewModel() {

    private val _events = MutableSharedFlow<DevicesEvent>()
    val events: SharedFlow<DevicesEvent>
        get() = _events.asSharedFlow()
    val devices = repository.getDevices().flow

    fun goBack() = navigationDispatcher.emit { it.navigateUp() }
}
