package com.spaceapps.myapplication.app.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
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
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
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

    private var startDestination: String? = null

    @OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setupEdgeToEdge()
        observeSplashScreenVisibility {
            setContent {
                val lifecycleOwner = LocalLifecycleOwner.current
                val systemUiController = rememberSystemUiController()
                val navController = rememberAnimatedNavController()
                val bottomSheetNavigator = rememberBottomSheetNavigator()

                navController.navigatorProvider += bottomSheetNavigator

                val navigationEvents = rememberNavigationEmitter(lifecycleOwner)
                val unauthorizedEvents = rememberUnauthorizedEvents(lifecycleOwner)

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

                val bottomItems = provideBottomItems()
                val currentDestination by navController.currentBackStackEntryAsState()
                val isBottomBarVisible = when (currentDestination?.destination?.route) {
                    About.route,
                    GeolocationMap.route,
                    Profile.route,
                    SaveLocation.route -> true
                    else -> false
                }

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
        lifecycleScope.launch {
            withContext(Dispatchers.IO) { startDestination = provideStartDestination() }
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
            null -> if (dataStoreManager.getOnBoardingPassed()) Auth.route else OnBoarding.route
            else -> GeolocationMap.route
        }
    }

    @Composable
    private fun rememberNavigationEmitter(lifecycleOwner: LifecycleOwner): Flow<NavigationCommand> {
        return remember(navDispatcher.emitter, lifecycleOwner) {
            navDispatcher.emitter.flowWithLifecycle(
                lifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            )
        }
    }

    @Composable
    private fun rememberUnauthorizedEvents(lifecycleOwner: LifecycleOwner): Flow<Boolean> {
        return remember(authDispatcher.emitter, lifecycleOwner) {
            authDispatcher.emitter.flowWithLifecycle(
                lifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            )
        }
    }

    private fun observeSplashScreenVisibility(onReadyToDraw: () -> Unit) {
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                return if (startDestination != null) {
                    content.viewTreeObserver.removeOnPreDrawListener(this)
                    onReadyToDraw()
                    true
                } else {
                    false
                }
            }
        })
    }
}
