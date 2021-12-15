package com.spaceapps.myapplication.app.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.spaceapps.myapplication.ui.SpaceAppsTheme
import com.spaceapps.myapplication.utils.NavigationDispatcher
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@ExperimentalWearMaterialApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationDispatcher: NavigationDispatcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberSwipeDismissableNavController()
            LaunchedEffect(Unit) {
                navigationDispatcher.emitter.onEach { event -> event(navController) }.launchIn(this)
            }
            SpaceAppsTheme {
                PopulatedNavHost(
                    navController = navController,
                    startDestination = provideStartDestination()
                )
            }
        }
    }

    private fun provideStartDestination(): String {
        return Screen.Home.route
    }
}
