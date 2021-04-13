package com.spaceapps.myapplication.features.settings

sealed class SettingsEvent
object InitSettingsState : SettingsEvent()
object ShowLogOutDialog : SettingsEvent()