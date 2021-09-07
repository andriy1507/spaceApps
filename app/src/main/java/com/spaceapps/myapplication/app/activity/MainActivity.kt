package com.spaceapps.myapplication.app.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.plusAssign
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.spaceapps.myapplication.app.GeolocationGraph
import com.spaceapps.myapplication.app.Screens
import com.spaceapps.myapplication.app.local.DataStoreManager
import com.spaceapps.myapplication.app.local.SpaceAppsDatabase
import com.spaceapps.myapplication.ui.SpaceAppsTheme
import com.spaceapps.myapplication.utils.AuthDispatcher
import com.spaceapps.myapplication.utils.NavigationDispatcher
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var authDispatcher: AuthDispatcher

    @Inject
    lateinit var navDispatcher: NavigationDispatcher

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    @Inject
    lateinit var dataBase: SpaceAppsDatabase

    @OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupEdgeToEdge()
        setContent {
            val bottomSheetNavigator = rememberBottomSheetNavigator()
            val navController = rememberAnimatedNavController()
            navController.navigatorProvider += bottomSheetNavigator
            val lifecycleOwner = LocalLifecycleOwner.current
            val navigationEvents = remember(navDispatcher.emitter, lifecycleOwner) {
                navDispatcher.emitter.flowWithLifecycle(
                    lifecycleOwner.lifecycle,
                    Lifecycle.State.STARTED
                )
            }
            val unauthorizedEvents = remember(authDispatcher.emitter, lifecycleOwner) {
                authDispatcher.emitter.flowWithLifecycle(
                    lifecycleOwner.lifecycle,
                    Lifecycle.State.STARTED
                )
            }
            LaunchedEffect(Unit) {
                launch {
                    navigationEvents.collect { event -> event(navController) }
                }
                launch {
                    unauthorizedEvents.collect {
                        when (it) {
                            true -> logOut()
                            false -> restart()
                        }
                    }
                }
            }
            SpaceAppsTheme {
                ModalBottomSheetLayout(bottomSheetNavigator) {
                    Scaffold {
                        PopulatedNavHost(navController, Screens.Auth.route, it)
                    }
                }
            }
        }
    }

    private fun logOut() = lifecycleScope.launch(Dispatchers.IO) {
        dataStoreManager.clearData()
        dataBase.clearAllTables()
        restart()
    }

    private fun restart() {
        finish()
        startActivity(intent)
    }

    private fun setupEdgeToEdge() = WindowCompat.setDecorFitsSystemWindows(window, false)
}
