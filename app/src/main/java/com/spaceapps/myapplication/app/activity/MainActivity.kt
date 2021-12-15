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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.plusAssign
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.app.Screens.*
import com.spaceapps.myapplication.core.local.DataStoreManager
import com.spaceapps.myapplication.core.local.DatabaseManager
import com.spaceapps.myapplication.core.utils.AuthDispatcher
import com.spaceapps.myapplication.utils.NavigationCommand
import com.spaceapps.myapplication.utils.NavigationDispatcher
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
    lateinit var databaseManager: DatabaseManager

    @Inject
    lateinit var dataStoreManager: DataStoreManager

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
            val currentDestination by navController.currentBackStackEntryAsState()
            val isBottomBarVisible = when (currentDestination?.destination?.route) {
                About.route,
                GeolocationMap.route,
                Profile.route,
                SaveLocation.route -> true
                else -> false
            }
            val systemUiController = rememberSystemUiController()
            val useDarkIcons = MaterialTheme.colors.isLight
            SideEffect {
                systemUiController.setSystemBarsColor(Color.Transparent, useDarkIcons)
            }
            MainScreen(
                isBottomBarVisible = isBottomBarVisible,
                bottomItems = bottomItems,
                navController = navController,
                startDestination = provideStartDestination()
            )
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
            GeolocationMap.route,
            Icons.Filled.MyLocation,
            R.string.location
        ),
        MenuItem(
            Profile.route,
            Icons.Filled.Person,
            R.string.profile
        ),
        MenuItem(
            About.route,
            Icons.Filled.Info,
            R.string.about
        )
    )

    @Composable
    private fun ObserveEvents(navController: NavHostController) {
        val navigationEvents = rememberNavigationEmitter()
        val unauthorizedEvents = rememberUnauthorizedEvents()
        LaunchedEffect(Unit) {
            navigationEvents.onEach { event -> event(navController) }.launchIn(this)
            unauthorizedEvents.onEach {
                when (it) {
                    true -> logOut()
                    false -> restart()
                }
            }.launchIn(this)
        }
    }

    private fun logOut() = lifecycleScope.launch(Dispatchers.IO) {
        databaseManager.clear()
        dataStoreManager.clearData()
        restart()
    }

    private fun restart() {
        finish()
        startActivity(intent)
    }

    private fun setupEdgeToEdge() = WindowCompat.setDecorFitsSystemWindows(window, false)

    private fun provideStartDestination() = runBlocking {
        when (dataStoreManager.getAccessToken()) {
            null -> Auth.route
            else -> GeolocationMap.route
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
