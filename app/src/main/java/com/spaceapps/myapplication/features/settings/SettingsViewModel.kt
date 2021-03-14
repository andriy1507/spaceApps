package com.spaceapps.myapplication.features.settings

import androidx.lifecycle.ViewModel
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.local.SettingsStorage
import com.spaceapps.myapplication.repositories.AuthRepository
import com.spaceapps.myapplication.utils.AuthDispatcher
import com.spaceapps.myapplication.utils.NavDispatcher
import com.spaceapps.myapplication.utils.async
import com.spaceapps.myapplication.utils.request
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val navDispatcher: NavDispatcher,
    private val authDispatcher: AuthDispatcher,
    private val authRepository: AuthRepository,
    private val settingsStorage: SettingsStorage
) : ViewModel() {

    fun goAuth() = navDispatcher.emit { navigate(R.id.authScreen) }

    fun goChat() = navDispatcher.emit { navigate(R.id.chatScreen) }

    fun goFeeds() = navDispatcher.emit { navigate(R.id.feedsListScreen) }

    fun logOut() = async {
        request { authRepository.logOut() }.onSuccess { authDispatcher.requestDeauthorization() }
    }
}
