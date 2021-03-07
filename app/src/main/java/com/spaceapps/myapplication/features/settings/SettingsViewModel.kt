package com.spaceapps.myapplication.features.settings

import androidx.lifecycle.ViewModel
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.utils.NavDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val navDispatcher: NavDispatcher
) : ViewModel() {

    fun goAuth() = navDispatcher.emit { navigate(R.id.authScreen) }

    fun goChat() = navDispatcher.emit { navigate(R.id.chatScreen) }
}
