package com.spaceapps.myapplication.app.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.plusAssign
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.app.AboutGraph
import com.spaceapps.myapplication.app.GeolocationGraph
import com.spaceapps.myapplication.app.ProfileGraph
import com.spaceapps.myapplication.app.Screens
import com.spaceapps.myapplication.app.local.DataStoreManager
import com.spaceapps.myapplication.app.local.SpaceAppsDatabase
import com.spaceapps.myapplication.ui.ACTION_BAR_SIZE
import com.spaceapps.myapplication.ui.SpaceAppsTheme
import com.spaceapps.myapplication.utils.AuthDispatcher
import com.spaceapps.myapplication.utils.NavigationCommand
import com.spaceapps.myapplication.utils.NavigationDispatcher
import com.spaceapps.myapplication.utils.navigateToRootDestination
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
        installSplashScreen()
        setContent {
            val bottomSheetNavigator = rememberBottomSheetNavigator()
            val navController = rememberAnimatedNavController()
            navController.navigatorProvider += bottomSheetNavigator
            ObserveEvents(navController)
            val bottomItems = provideBottomItems()
            var selectedIndex by remember { mutableStateOf(0) }
            val currentDestination by navController.currentBackStackEntryAsState()
            val isBottomBarVisible = when (currentDestination?.destination?.route) {
                AboutGraph.About.route,
                GeolocationGraph.GeolocationMap.route,
                ProfileGraph.Profile.route -> true
                else -> false
            }
            val systemUiController = rememberSystemUiController()
            val useDarkIcons = MaterialTheme.colors.isLight
            SideEffect {
                systemUiController.setSystemBarsColor(Color.Transparent, useDarkIcons)
            }
            SpaceAppsTheme {
                Scaffold(
                    bottomBar = {
                        AnimatedVisibility(
                            visible = isBottomBarVisible,
                            enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
                            exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top)
                        ) {
                            BottomNavigation(
                                modifier = Modifier.navigationBarsHeight(ACTION_BAR_SIZE)
                            ) {
                                bottomItems.forEachIndexed { index, menuItem ->
                                    BottomNavigationItem(
                                        modifier = Modifier.navigationBarsPadding(),
                                        selected = selectedIndex == index,
                                        onClick = {
                                            selectedIndex = index
                                            navController.navigateToRootDestination(menuItem.route)
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = menuItem.icon,
                                                contentDescription = stringResource(id = menuItem.labelId)
                                            )
                                        },
                                        label = { Text(text = stringResource(id = menuItem.labelId)) }
                                    )
                                }
                            }
                        }
                    }
                ) {
                    PopulatedNavHost(navController, provideStartDestination(), it) {
                        selectedIndex = 0
                        navController.navigateToRootDestination(GeolocationGraph.route)
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent ?: return
        Firebase.dynamicLinks.getDynamicLink(intent).addOnSuccessListener { data ->
            val link = data.link ?: return@addOnSuccessListener
            navDispatcher.emit { it.navigate(link) }
        }
    }

    private fun provideBottomItems() = listOf(
        MenuItem(
            GeolocationGraph.route,
            Icons.Filled.MyLocation,
            R.string.location
        ),
        MenuItem(
            ProfileGraph.route,
            Icons.Filled.Person,
            R.string.profile
        ),
        MenuItem(
            AboutGraph.route,
            Icons.Filled.Info,
            R.string.about
        )
    )

    @Composable
    private fun ObserveEvents(navController: NavHostController) {
        val navigationEvents = rememberNavigationEmitter()
        val unauthorizedEvents = rememberUnauthorizedEvents()
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

    private fun provideStartDestination() = runBlocking {
        when (dataStoreManager.getAccessToken()) {
            null -> Screens.Auth.route
            else -> GeolocationGraph.route
        }
    }

    @Composable
    private fun rememberNavigationEmitter(): Flow<NavigationCommand> {
        val lifecycleOwner = LocalLifecycleOwner.current
        return remember(navDispatcher.emitter, lifecycleOwner) {
            navDispatcher.emitter.flowWithLifecycle(
                lifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            )
        }
    }

    @Composable
    private fun rememberUnauthorizedEvents(): Flow<Boolean> {
        val lifecycleOwner = LocalLifecycleOwner.current
        return remember(authDispatcher.emitter, lifecycleOwner) {
            authDispatcher.emitter.flowWithLifecycle(
                lifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            )
        }
    }
}
