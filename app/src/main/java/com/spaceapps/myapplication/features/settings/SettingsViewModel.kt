package com.spaceapps.myapplication.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spaceapps.myapplication.core.DEGREES_DMS
import com.spaceapps.myapplication.core.SYSTEM_GEO
import com.spaceapps.myapplication.core.local.DataStoreManager
import com.spaceapps.myapplication.core.utils.DispatchersProvider
import com.spaceapps.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val dispatchersProvider: DispatchersProvider,
    private val navigator: Navigator
) : ViewModel() {

    val format = dataStoreManager.observeDegreesFormat()
        .stateIn(viewModelScope, SharingStarted.Lazily, DEGREES_DMS)
    val system = dataStoreManager.observeCoordSystem()
        .stateIn(viewModelScope, SharingStarted.Lazily, SYSTEM_GEO)

    fun onFormatClick(format: String) = viewModelScope.launch(dispatchersProvider.IO) {
        dataStoreManager.setDegreesFormat(format)
    }

    fun onSystemClick(system: String) = viewModelScope.launch(dispatchersProvider.IO) {
        dataStoreManager.setCoordinatesSystem(system)
    }
    fun goBack() = navigator.emit { it.navigateUp() }
}
