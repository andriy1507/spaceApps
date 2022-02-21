package com.spaceapps.myapplication.features.location.locationsList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.spaceapps.myapplication.core.repositories.locations.LocationsRepository
import com.spaceapps.myapplication.core.utils.getStateFlow
import com.spaceapps.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationsListViewModel @Inject constructor(
    private val repository: LocationsRepository,
    private val navigator: Navigator,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val searchQuery = savedStateHandle.getLiveData<String?>("searchQuery", null)
    val isSearchEnabled = savedStateHandle.getStateFlow(
        scope = viewModelScope,
        key = "isSearchEnabled",
        initialValue = false
    )
    val locations = searchQuery.asFlow()
        .distinctUntilChanged()
        .conflate()
        .map { repository.getLocationsByName(it).flow }

    fun onSearchQueryEnter(input: String) = searchQuery.postValue(input)

    fun deleteLocation(id: Int) = viewModelScope.launch {
        repository.deleteLocation(id)
    }

    fun goBack() = navigator.emit { it.navigateUp() }

    fun onCloseSearchClicked() = viewModelScope.launch {
        searchQuery.postValue(null)
        if (searchQuery.value.isNullOrBlank()) isSearchEnabled.emit(false)
    }

    fun onSearchClicked() = viewModelScope.launch {
        isSearchEnabled.emit(true)
    }
}
