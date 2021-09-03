package com.spaceapps.myapplication.app.activity

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.spaceapps.myapplication.app.Navigation
import com.spaceapps.myapplication.features.geolocation.GeolocationScreen

@Composable
fun PopulatedNavHost(navController: NavHostController, startDestination: String) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Navigation.geolocation) {
            GeolocationScreen(hiltViewModel(it))
        }
    }
}
