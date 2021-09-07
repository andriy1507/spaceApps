package com.spaceapps.myapplication.app.activity

import androidx.activity.compose.BackHandler
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.spaceapps.myapplication.app.AboutGraph
import com.spaceapps.myapplication.app.GeolocationGraph
import com.spaceapps.myapplication.app.Screens
import com.spaceapps.myapplication.features.about.AboutScreen
import com.spaceapps.myapplication.features.auth.AuthScreen
import com.spaceapps.myapplication.features.geolocation.GeolocationMapScreen

@OptIn(ExperimentalAnimationApi::class)
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
            AuthScreen()
        }

        navigation(
            startDestination = GeolocationGraph.GeolocationMap.route,
            route = GeolocationGraph.route,
            enterTransition = { _, _ -> EnterTransition.None },
            exitTransition = { _, _ -> ExitTransition.None },
            popEnterTransition = { _, _ -> EnterTransition.None },
            popExitTransition = { _, _ -> ExitTransition.None }
        ) {
            composable(GeolocationGraph.GeolocationMap.route) {
                GeolocationMapScreen(hiltViewModel(it))
            }
        }

        navigation(
            startDestination = AboutGraph.About.route,
            route = AboutGraph.route,
            enterTransition = { _, _ -> EnterTransition.None },
            exitTransition = { _, _ -> ExitTransition.None },
            popEnterTransition = { _, _ -> EnterTransition.None },
            popExitTransition = { _, _ -> ExitTransition.None }
        ) {
            composable(AboutGraph.About.route) {
                onBackPressIntercepted?.let { onBack -> BackHandler(onBack = onBack) }
                AboutScreen()
            }
        }
    }
}
