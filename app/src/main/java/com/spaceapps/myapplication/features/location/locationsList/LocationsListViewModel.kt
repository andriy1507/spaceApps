package com.spaceapps.myapplication.features.location.locationsList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.spaceapps.myapplication.app.repositories.locations.LocationsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class LocationsListViewModel @Inject constructor(
    private val repository: LocationsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel()
