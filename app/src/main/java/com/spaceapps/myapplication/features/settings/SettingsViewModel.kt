package com.spaceapps.myapplication.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.spaceapps.myapplication.Settings
import com.spaceapps.myapplication.local.SettingsStorage
import com.spaceapps.myapplication.repositories.auth.AuthRepository
import com.spaceapps.myapplication.repositories.auth.LogOutResult
import com.spaceapps.myapplication.utils.AuthDispatcher
import com.spaceapps.myapplication.utils.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authDispatcher: AuthDispatcher,
    private val authRepository: AuthRepository,
    private val settingsStorage: SettingsStorage
) : ViewModel() {

    val language = settingsStorage.observeLanguage().asLiveData()

    fun setLanguage(lang: Settings.Language) = launch {
        settingsStorage.setLanguage(language = lang)
        authDispatcher.requestRestart()
    }

    fun logOut() = launch {
        when (authRepository.logOut()) {
            LogOutResult.Success -> authDispatcher.requestLogOut()
            LogOutResult.Failure -> Unit
        }
    }
}
