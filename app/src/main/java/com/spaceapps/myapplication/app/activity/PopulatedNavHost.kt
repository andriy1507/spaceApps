@file:OptIn(ExperimentalAnimationApi::class)
package com.spaceapps.myapplication.app.activity

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.AnimatedContentScope.SlideDirection.Companion.End
import androidx.compose.animation.AnimatedContentScope.SlideDirection.Companion.Start
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.dialog
import androidx.navigation.navDeepLink
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet
import com.spaceapps.myapplication.app.DeepLinks
import com.spaceapps.myapplication.app.Screens
import com.spaceapps.myapplication.features.about.AboutScreen
import com.spaceapps.myapplication.features.auth.AuthScreen
import com.spaceapps.myapplication.features.devices.DevicesScreen
import com.spaceapps.myapplication.features.forgotPassword.ForgotPasswordScreen
import com.spaceapps.myapplication.features.location.locationsList.LocationsListScreen
import com.spaceapps.myapplication.features.location.map.GeolocationMapScreen
import com.spaceapps.myapplication.features.location.saveLocation.SaveLocationScreen
import com.spaceapps.myapplication.features.notificationView.NotificationViewScreen
import com.spaceapps.myapplication.features.notifications.NotificationsScreen
import com.spaceapps.myapplication.features.player.PlayerScreen
import com.spaceapps.myapplication.features.profile.ProfileScreen
import com.spaceapps.myapplication.features.resetPassword.ResetPasswordScreen
import com.spaceapps.myapplication.features.settings.SettingsScreen
import com.spaceapps.myapplication.features.socialAuth.SocialAuthScreen
import com.spaceapps.myapplication.features.termsPolicy.TermsPolicyScreen

@OptIn(ExperimentalMaterialNavigationApi::class)
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
        composable(route = Screens.Auth.route) {
            AuthScreen(hiltViewModel(it))
        }
        composable(route = Screens.ForgotPassword.route) {
            ForgotPasswordScreen(hiltViewModel(it))
        }
        composable(
            route = Screens.ResetPassword.route,
            deepLinks = listOf(navDeepLink { uriPattern = DeepLinks.ResetPassword.uri }),
            arguments = DeepLinks.ResetPassword.args
        ) {
            ResetPasswordScreen(hiltViewModel(it))
        }
        bottomSheet(route = Screens.SocialAuth.route) {
            SocialAuthScreen()
        }
        composable(route = Screens.GeolocationMap.route) {
            GeolocationMapScreen(hiltViewModel(it))
        }
        composable(route = Screens.MapSettings.route) {
            SettingsScreen(hiltViewModel(it))
        }
        dialog(route = Screens.SaveLocation.route) {
            SaveLocationScreen(hiltViewModel(it))
        }
        composable(route = Screens.LocationsList.route) {
            LocationsListScreen(hiltViewModel(it))
        }
        composable(
            route = Screens.Profile.route,
            popEnterTransition = Transitions.PopEnterTransition,
            exitTransition = { _, target ->
                when (target.destination.route) {
                    Screens.Notifications.route,
                    Screens.Devices.route -> slideOutOfContainer(Start)
                    else -> null
                }
            }
        ) {
            onBackPressIntercepted?.let { onBack -> BackHandler(onBack = onBack) }
            ProfileScreen(hiltViewModel(it))
        }
        composable(
            route = Screens.Notifications.route,
            popEnterTransition = Transitions.PopEnterTransition,
            exitTransition = Transitions.ExitTransition,
            enterTransition = Transitions.EnterTransition,
            popExitTransition = Transitions.PopExitTransition
        ) {
            NotificationsScreen(hiltViewModel(it))
        }
        composable(
            route = Screens.NotificationView.route,
            arguments = DeepLinks.NotificationView.args,
            deepLinks = listOf(navDeepLink { uriPattern = DeepLinks.NotificationView.uri }),
            enterTransition = Transitions.EnterTransition,
            popExitTransition = Transitions.PopExitTransition
        ) {
            NotificationViewScreen(hiltViewModel(it))
        }
        composable(
            route = Screens.Devices.route,
            enterTransition = Transitions.EnterTransition,
            popExitTransition = Transitions.PopExitTransition
        ) {
            DevicesScreen(hiltViewModel(it))
        }
        composable(route = Screens.Player.route) {
            PlayerScreen(hiltViewModel(it))
        }
        composable(route = Screens.About.route) {
            onBackPressIntercepted?.let { onBack -> BackHandler(onBack = onBack) }
            AboutScreen()
        }
        composable(route = Screens.TermsPolicy.route) {
            TermsPolicyScreen()
        }
    }
}

private typealias EnterTrans = AnimatedContentScope<String>.(
    initial: NavBackStackEntry,
    target: NavBackStackEntry
) -> EnterTransition
private typealias ExitTrans = AnimatedContentScope<String>.(
    initial: NavBackStackEntry,
    target: NavBackStackEntry
) -> ExitTransition

private object Transitions {
    val EnterTransition: EnterTrans = { _, _ ->
        slideIntoContainer(Start) + fadeIn(initialAlpha = .5f)
    }
    val PopEnterTransition: EnterTrans = { _, _ ->
        slideIntoContainer(End)
    }
    val ExitTransition: ExitTrans = { _, _ ->
        slideOutOfContainer(Start)
    }
    val PopExitTransition: ExitTrans = { _, _ ->
        slideOutOfContainer(End) + fadeOut(targetAlpha = .5f)
    }
}
