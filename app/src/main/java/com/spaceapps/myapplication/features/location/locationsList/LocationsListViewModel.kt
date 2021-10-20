package com.spaceapps.myapplication.features.location.locationsList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import com.spaceapps.myapplication.app.repositories.locations.LocationsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class LocationsListViewModel @Inject constructor(
    private val repository: LocationsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val searchQuery = savedStateHandle.getLiveData("searchQuery", "")
    val locations = searchQuery.asFlow().distinctUntilChanged()
        .map { repository.getLocationsByName(it).flow }

    fun onSearchQueryEnter(input: String) = searchQuery.postValue(input)
}
