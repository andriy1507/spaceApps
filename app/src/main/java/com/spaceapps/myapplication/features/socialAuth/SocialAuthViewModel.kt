package com.spaceapps.myapplication.features.socialAuth

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.spaceapps.myapplication.core.repositories.auth.AuthRepository
import com.spaceapps.myapplication.core.utils.AuthDispatcher
import com.spaceapps.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SocialAuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val navigator: Navigator,
    private val authDispatcher: AuthDispatcher,
    savedStateHandle: SavedStateHandle
) : ViewModel()
