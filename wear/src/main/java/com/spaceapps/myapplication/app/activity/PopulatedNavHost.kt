package com.spaceapps.myapplication.app.activity

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import com.spaceapps.myapplication.features.home.HomeScreen

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun PopulatedNavHost(
    navController: NavHostController,
    startDestination: String
) {
    SwipeDismissableNavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Home.route) {
            HomeScreen(hiltViewModel(it))
        }
        composable(Screen.Notifications.route) {
        }
    }
}
