package com.spaceapps.myapplication.app

import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.spaceapps.myapplication.Settings
import com.spaceapps.myapplication.app.Navigation.geolocation
import com.spaceapps.myapplication.app.Navigation.notifications
import com.spaceapps.myapplication.app.Navigation.qrCode
import com.spaceapps.myapplication.app.Navigation.settings
import com.spaceapps.myapplication.features.notifications.NotificationsScreen
import com.spaceapps.myapplication.features.settings.SettingsScreen
import com.spaceapps.myapplication.features.settings.SettingsViewModel
import com.spaceapps.myapplication.ui.SpaceAppsTheme

@Preview
@Composable
fun MainScreen() = SpaceAppsTheme {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomBar(navController) }
    ) {
        NavHost(navController = navController, startDestination = settings) {
            composable(settings) {
                val vm = hiltNavGraphViewModel<SettingsViewModel>(it)
                val language by vm.language.observeAsState(Settings.Language.UNRECOGNIZED)
                SettingsScreen(
                    language = language,
                    onLogOutClick = vm::logOut,
                    onChangeLanguage = vm::setLanguage
                )
            }
            composable(notifications) {
                NotificationsScreen()
            }
            composable(geolocation) {

            }
            composable(qrCode) {

            }
        }
    }
}

@Composable
fun BottomBar(controller: NavController) = BottomAppBar {
    BottomNavigationItem(
        selected = false,
        onClick = { controller.navigate(geolocation) },
        icon = {}
    )
    BottomNavigationItem(
        selected = false,
        onClick = { controller.navigate(notifications) },
        icon = {}
    )
    BottomNavigationItem(
        selected = false,
        onClick = { controller.navigate(settings) },
        icon = {}
    )
}