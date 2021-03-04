package com.spaceapps.myapplication

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.spaceapps.myapplication.local.StorageManager
import com.spaceapps.myapplication.ui.ACTION_BAR_SIZE
import com.spaceapps.myapplication.ui.SpaceAppsTheme
import com.spaceapps.myapplication.utils.AuthDispatcher
import com.spaceapps.myapplication.utils.NavDispatcher
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.accompanist.insets.LocalWindowInsets
import dev.chrisbanes.accompanist.insets.navigationBarsHeight
import dev.chrisbanes.accompanist.insets.toPaddingValues
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SpaceAppsMainActivity : AppCompatActivity() {

    @Inject
    lateinit var navDispatcher: NavDispatcher

    @Inject
    lateinit var authDispatcher: AuthDispatcher

    @Inject
    lateinit var storageManager: StorageManager

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

    private suspend fun observeAuthState() {
        for (e in authDispatcher.emitter) {
            if (e) unauthorize()
        }
    }

    private fun unauthorize() = lifecycleScope.launch {
        storageManager.clear()
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

    private fun isCurrentDestination(@IdRes destId: Int, otherAction: (() -> Unit)? = null): Boolean {
        val isCurrent = navController?.currentDestination?.id == destId
        if (!isCurrent) otherAction?.invoke()
        return isCurrent
    }

    @Composable
    fun MainActivityScreen() {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = { ApplicationBottomBar() },
            content = { NavView() }
        )
    }

    @Composable
    fun ApplicationBottomBar() = BottomAppBar(
        modifier = Modifier.navigationBarsHeight(additional = ACTION_BAR_SIZE.dp),
        contentPadding = LocalWindowInsets.current.navigationBars.toPaddingValues()
    ) {
        BottomNavigationItem(
            selected = isCurrentDestination(R.id.geolocationScreen),
            onClick = {
                isCurrentDestination(R.id.geolocationScreen) {
                    navDispatcher.emit { navigate(R.id.geolocationScreen) }
                }
            },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_location),
                    contentDescription = null
                )
            },
            label = { Text(text = stringResource(R.string.location)) }
        )
        BottomNavigationItem(
            selected = isCurrentDestination(R.id.notificationsScreen),
            onClick = {
                isCurrentDestination(R.id.notificationsScreen) {
                    navDispatcher.emit { navigate(R.id.notificationsScreen) }
                }
            },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_notifications),
                    contentDescription = null
                )
            },
            label = { Text(text = stringResource(R.string.notifications)) }
        )
        BottomNavigationItem(
            selected = isCurrentDestination(R.id.settingsScreen),
            onClick = {
                isCurrentDestination(R.id.settingsScreen) {
                    navDispatcher.emit { navigate(R.id.settingsScreen) }
                }
            },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_settings),
                    contentDescription = null
                )
            },
            label = { Text(text = stringResource(R.string.settings)) }
        )
    }

    @Composable
    private fun NavView() {
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
