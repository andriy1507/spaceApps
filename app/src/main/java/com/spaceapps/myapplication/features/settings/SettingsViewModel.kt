package com.spaceapps.myapplication.features.settings

import androidx.lifecycle.ViewModel
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.local.SettingsStorage
import com.spaceapps.myapplication.repositories.auth.AuthRepository
import com.spaceapps.myapplication.repositories.auth.LogOutResult
import com.spaceapps.myapplication.utils.AuthDispatcher
import com.spaceapps.myapplication.utils.NavDispatcher
import com.spaceapps.myapplication.utils.async
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val navDispatcher: NavDispatcher,
    private val authDispatcher: AuthDispatcher,
    private val authRepository: AuthRepository,
    private val settingsStorage: SettingsStorage
) : ViewModel() {

    val events = MutableSharedFlow<SettingsEvent>()

    fun goAuth() = navDispatcher.emit { navigate(R.id.authScreen) }

    fun goChat() = navDispatcher.emit { navigate(R.id.chatScreen) }

    fun goFeeds() = navDispatcher.emit { navigate(R.id.feedsListScreen) }

    fun showLogOut() = async { events.emit(ShowLogOutDialog) }

    fun dismissLogOut() = async { events.emit(InitSettingsState) }

    fun logOut() = async {
        when (authRepository.logOut()) {
            LogOutResult.Success -> authDispatcher.requestDeauthorization()
            LogOutResult.Failure -> Unit
        }
    }
}
