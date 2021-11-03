package com.spaceapps.myapplication.app.activity

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Notifications : Screen("notifications")
}

sealed class DeepLink(val route: String)
