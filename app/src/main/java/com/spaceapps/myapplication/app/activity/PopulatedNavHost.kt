package com.spaceapps.myapplication.app.activity

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.dialog
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet
import com.spaceapps.myapplication.app.AboutGraph
import com.spaceapps.myapplication.app.GeolocationGraph
import com.spaceapps.myapplication.app.Screens
import com.spaceapps.myapplication.features.about.AboutScreen
import com.spaceapps.myapplication.features.auth.AuthScreen
import com.spaceapps.myapplication.features.forgotPassword.ForgotPasswordScreen
import com.spaceapps.myapplication.features.location.locationsList.LocationsListScreen
import com.spaceapps.myapplication.features.location.map.GeolocationMapScreen
import com.spaceapps.myapplication.features.location.saveLocation.SaveLocationScreen
import com.spaceapps.myapplication.features.settings.SettingsScreen
import com.spaceapps.myapplication.features.socialAuth.SocialAuthScreen
import com.spaceapps.myapplication.features.termsPolicy.TermsPolicyScreen

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialNavigationApi::class)
@Composable
fun PopulatedNavHost(
    navController: NavHostController,
    startDestination: String,
    innerPadding: PaddingValues,
    onBackPressIntercepted: (() -> Unit)? = null
) {
    AnimatedNavHost(
        modifier = Modifier.padding(innerPadding),
        navController = navController,
        startDestination = startDestination
    ) {

        composable(Screens.Auth.route) {
            AuthScreen(hiltViewModel(it))
        }

        composable(Screens.ForgotPassword.route) {
            ForgotPasswordScreen()
        }

        bottomSheet(Screens.SocialAuth.route) {
            SocialAuthScreen()
        }

        navigation(
            startDestination = GeolocationGraph.GeolocationMap.route,
            route = GeolocationGraph.route
        ) {
            composable(GeolocationGraph.GeolocationMap.route) {
                GeolocationMapScreen(hiltViewModel(it))
            }
            composable(GeolocationGraph.MapSettings.route) {
                SettingsScreen(hiltViewModel(it))
            }
            dialog(GeolocationGraph.SaveLocation.route) {
                SaveLocationScreen()
            }
            composable(GeolocationGraph.LocationsList.route) {
                LocationsListScreen(hiltViewModel(it))
            }
        }

        navigation(
            startDestination = AboutGraph.About.route,
            route = AboutGraph.route
        ) {
            composable(AboutGraph.About.route) {
                onBackPressIntercepted?.let { onBack -> BackHandler(onBack = onBack) }
                AboutScreen()
            }
            composable(AboutGraph.TermsPolicy.route) {
                TermsPolicyScreen()
            }
        }
    }
}
