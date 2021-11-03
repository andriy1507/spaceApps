package com.spaceapps.myapplication.features.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.app.activity.Screen
import com.spaceapps.myapplication.core.utils.getStateFlow
import com.spaceapps.myapplication.utils.NavigationDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val navigationDispatcher: NavigationDispatcher,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val menuItems = savedStateHandle.getStateFlow<List<HomeMenuItem>>(
        scope = viewModelScope,
        key = "",
        initialValue = emptyList()
    )

    init {
        val items: List<HomeMenuItem> = listOf(
            HomeMenuItem(titleRes = R.string.notifications, route = Screen.Notifications.route)
        )
        menuItems.tryEmit(items)
    }

    fun onItemClick(route: String) = navigationDispatcher.emit { it.navigate(route) }
}
