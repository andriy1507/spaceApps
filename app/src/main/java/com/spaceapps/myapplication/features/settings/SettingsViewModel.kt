package com.spaceapps.myapplication.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spaceapps.myapplication.app.DEGREES_DMS
import com.spaceapps.myapplication.app.SYSTEM_GEO
import com.spaceapps.myapplication.app.local.DataStoreManager
import com.spaceapps.myapplication.utils.DispatchersProvider
import com.spaceapps.myapplication.utils.NavigationDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val dispatchersProvider: DispatchersProvider,
    private val navigationDispatcher: NavigationDispatcher
) : ViewModel() {

    val format = dataStoreManager.observeDegreesFormat()
        .stateIn(viewModelScope, SharingStarted.Lazily, DEGREES_DMS)
    val system = dataStoreManager.observeCoordSystem()
        .stateIn(viewModelScope, SharingStarted.Lazily, SYSTEM_GEO)

    fun onFormatClick(format: String) = viewModelScope.launch(dispatchersProvider.io) {
        dataStoreManager.setDegreesFormat(format)
    }

    fun onSystemClick(system: String) = viewModelScope.launch(dispatchersProvider.io) {
        dataStoreManager.setCoordinatesSystem(system)
    }
    fun goBack() = navigationDispatcher.emit { it.navigateUp() }
}
