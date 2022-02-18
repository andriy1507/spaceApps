package com.spaceapps.myapplication.features.onBoarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spaceapps.myapplication.app.Screens
import com.spaceapps.myapplication.core.local.DataStoreManager
import com.spaceapps.myapplication.utils.NavigationDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val navigationDispatcher: NavigationDispatcher,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<OnBoardingAction>()

    init {
        collectActions()
    }

    fun onActionSubmit(action: OnBoardingAction) = viewModelScope.launch {
        pendingActions.emit(action)
    }

    private fun collectActions() = viewModelScope.launch {
        pendingActions.collect { action ->
            when (action) {
                is OnBoardingAction.ContinueClick -> goAuth()
            }
        }
    }

    private fun goAuth() = viewModelScope.launch {
        dataStoreManager.setOnBoardingPassed(true)
        navigationDispatcher.emit {
            it.navigate(Screens.Auth.route) {
                popUpTo(Screens.OnBoarding.route) {
                    inclusive = true
                }
            }
        }
    }
}
