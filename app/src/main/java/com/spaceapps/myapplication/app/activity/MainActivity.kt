package com.spaceapps.myapplication.app.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.plusAssign
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.app.AboutGraph
import com.spaceapps.myapplication.app.GeolocationGraph
import com.spaceapps.myapplication.app.local.DataStoreManager
import com.spaceapps.myapplication.app.local.SpaceAppsDatabase
import com.spaceapps.myapplication.ui.SpaceAppsTheme
import com.spaceapps.myapplication.utils.AuthDispatcher
import com.spaceapps.myapplication.utils.NavigationDispatcher
import com.spaceapps.myapplication.utils.navigateToRootDestination
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
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
            val bottomItems = listOf(
                MenuItem(
                    GeolocationGraph.route,
                    R.drawable.ic_location,
                    R.string.location
                ),
                MenuItem(
                    AboutGraph.route,
                    R.drawable.ic_info,
                    R.string.about
                )
            )
            var selectedIndex by remember { mutableStateOf(0) }
            SpaceAppsTheme {
                Scaffold(bottomBar = {
                    BottomNavigation(modifier = Modifier.navigationBarsPadding()) {
                        bottomItems.forEachIndexed { index, menuItem ->
                            BottomNavigationItem(
                                selected = selectedIndex == index,
                                onClick = {
                                    selectedIndex = index
                                    navController.navigateToRootDestination(menuItem.route)
                                },
                                icon = {
                                    Icon(
                                        painter = painterResource(id = menuItem.iconId),
                                        contentDescription = null
                                    )
                                },
                                label = { Text(text = stringResource(id = menuItem.labelId)) }
                            )
                        }
                    }
                }) {
                    PopulatedNavHost(navController, GeolocationGraph.route, it) {
                        selectedIndex = 0
                        navController.navigateToRootDestination(GeolocationGraph.route)
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

    private fun setupEdgeToEdge() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }
}

data class MenuItem(
    val route: String,
    @DrawableRes val iconId: Int,
    @StringRes val labelId: Int
)
