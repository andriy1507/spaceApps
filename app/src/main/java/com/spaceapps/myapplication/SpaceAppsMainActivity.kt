package com.spaceapps.myapplication

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.spaceapps.myapplication.local.AuthTokenStorage
import com.spaceapps.myapplication.ui.ACTION_BAR_SIZE
import com.spaceapps.myapplication.ui.SpaceAppsTheme
import com.spaceapps.myapplication.utils.NavDispatcher
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.accompanist.insets.LocalWindowInsets
import dev.chrisbanes.accompanist.insets.navigationBarsHeight
import dev.chrisbanes.accompanist.insets.toPaddingValues
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SpaceAppsMainActivity : AppCompatActivity() {

    @Inject
    lateinit var authTokenStorage: AuthTokenStorage

    @Inject
    lateinit var navDispatcher: NavDispatcher

    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupEdgeToEdge()
        setTheme(R.style.Theme_MyApplication_NoActionBar)
        setContent {
            SpaceAppsTheme {
                MainActivityScreen()
            }
        }
        lifecycleScope.launchWhenCreated { observeAuthState() }
        lifecycleScope.launchWhenResumed { initNavigation() }
    }

    private fun observeAuthState() {
        authTokenStorage.authTokenFlow.onEach { token ->
            when (token) {
                "" -> unauthorize()
                else -> Unit
            }
        }.launchIn(lifecycleScope)
    }

    private fun unauthorize() = lifecycleScope.launch {
        authTokenStorage.clear()
        restart()
    }

    private fun restart() {
        finish()
        startActivity(intent)
    }

    private suspend fun initNavigation() {
        lifecycleScope.launchWhenResumed {
            for (event in NavDispatcher.events) navController?.event()
        }
    }

    private fun setupEdgeToEdge() {
        WindowCompat.setDecorFitsSystemWindows(window, true)
    }

    @Composable
    fun MainActivityScreen() {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomAppBar(
                    modifier = Modifier.navigationBarsHeight(additional = ACTION_BAR_SIZE.dp),
                    contentPadding = LocalWindowInsets.current.navigationBars.toPaddingValues()
                ) {
                    BottomNavigationItem(
                        selected = navController?.currentDestination?.id == R.id.geolocationScreen,
                        onClick = {
                            if (navController?.currentDestination?.id != R.id.geolocationScreen)
                                navDispatcher.emit { navigate(R.id.geolocationScreen) }
                        },
                        icon = {
                            Icon(
                                painter = painterResource(
                                    id = R.drawable.ic_location
                                ),
                                contentDescription = null
                            )
                        },
                        label = { Text(text = "Location") }
                    )
                    BottomNavigationItem(
                        selected = navController?.currentDestination?.id == R.id.notificationsScreen,
                        onClick = {
                            if (navController?.currentDestination?.id != R.id.notificationsScreen)
                                navDispatcher.emit { navigate(R.id.notificationsScreen) }
                        },
                        icon = {
                            Icon(
                                painter = painterResource(
                                    id = R.drawable.ic_notifications
                                ),
                                contentDescription = null
                            )
                        },
                        label = { Text(text = "Notifications") }
                    )
                    BottomNavigationItem(
                        selected = false,
                        onClick = { },
                        icon = {
                            Icon(
                                painter = painterResource(
                                    id = R.drawable.ic_settings
                                ),
                                contentDescription = null
                            )
                        },
                        label = { Text(text = "Settings") }
                    )
                }
            }
        ) {
            AndroidView(
                factory = { FragmentContainerView(it).apply { id = R.id.navHostFragment } }
            ) {
                val navHostFragment = NavHostFragment()
                supportFragmentManager.beginTransaction()
                    .add(R.id.navHostFragment, navHostFragment)
                    .commit()
                navHostFragment.lifecycle.addObserver(object : DefaultLifecycleObserver {
                    override fun onCreate(owner: LifecycleOwner) {
                        navHostFragment.navController.apply { setGraph(R.navigation.nav_graph) }
                            .also {
                                navController = it
                            }
                    }
                })
            }
        }
    }
}
