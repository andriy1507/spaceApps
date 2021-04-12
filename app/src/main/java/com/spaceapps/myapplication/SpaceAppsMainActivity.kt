package com.spaceapps.myapplication

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import com.spaceapps.myapplication.di.DataStoreModule.settingsDataStore
import com.spaceapps.myapplication.local.AuthTokenStorage
import com.spaceapps.myapplication.local.StorageManager
import com.spaceapps.myapplication.ui.ACTION_BAR_SIZE
import com.spaceapps.myapplication.ui.SpaceAppsTheme
import com.spaceapps.myapplication.utils.AuthDispatcher
import com.spaceapps.myapplication.utils.NavDispatcher
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.accompanist.insets.LocalWindowInsets
import dev.chrisbanes.accompanist.insets.navigationBarsHeight
import dev.chrisbanes.accompanist.insets.toPaddingValues
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SpaceAppsMainActivity : AppCompatActivity() {

    @Inject
    lateinit var navDispatcher: NavDispatcher

    @Inject
    lateinit var authDispatcher: AuthDispatcher

    @Inject
    lateinit var authTokenStorage: AuthTokenStorage

    @Inject
    lateinit var storageManager: StorageManager

    private var navController: NavController? = null

    private val navHostFragment = NavHostFragment()

    private val vm by viewModels<MainActivityViewModel>()

    override fun attachBaseContext(newBase: Context) = runBlocking {
//        val lang = when(settingsDataStore.data.first().language){
//            Settings.Language.Ukrainian -> "UK-ua"
//            else -> "en"
//        }
//        super.attachBaseContext(SpaceAppsContextWrapper.wrap(newBase, lang))
        super.attachBaseContext(newBase)
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration) = runBlocking{
//        val lang = when(settingsDataStore.data.first().language){
//            Settings.Language.Ukrainian -> "UK-ua"
//            else -> "en"
//        }
//        overrideConfiguration.setLocale(Locale(lang))
        super.applyOverrideConfiguration(overrideConfiguration)
    }

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

    private fun unauthorize() = lifecycleScope.launch(Dispatchers.IO) {
        storageManager.clear()
        restart()
    }

    private fun restart() {
        finish()
        startActivity(intent)
    }

    private suspend fun initNavigation() {
        lifecycleScope.launchWhenResumed {
            for (event in navDispatcher.events) navController?.event()
        }
    }

    private fun setupEdgeToEdge() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    private fun isCurrentDestination(
        @IdRes destId: Int,
        other: (() -> Unit)? = null
    ): Boolean {
        val isCurrent = navController?.currentDestination?.id == destId
        if (!isCurrent) other?.invoke()
        return isCurrent
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun MainActivityScreen() {
        val bottomBarVisible by vm.bottomBarVisible.observeAsState()
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                AnimatedVisibility(visible = bottomBarVisible == true, initiallyVisible = false) {
                    ApplicationBottomBar()
                }
            },
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
            factory = {
                val container = FragmentContainerView(it).apply { id = R.id.navHostFragment }
                supportFragmentManager.beginTransaction()
                    .replace(R.id.navHostFragment, navHostFragment)
                    .setPrimaryNavigationFragment(navHostFragment)
                    .commit()
                navHostFragment.lifecycle.addObserver(object : DefaultLifecycleObserver {
                    override fun onCreate(owner: LifecycleOwner) {
                        navHostFragment.navController.apply {
                            val navGraph = navInflater.inflate(R.navigation.nav_graph)
                            navGraph.startDestination = runBlocking {
                                if (authTokenStorage.getAuthToken() == null) {
                                    R.id.authScreen
                                } else {
                                    R.id.geolocationScreen
                                }
                            }
                            graph = navGraph
                            addOnDestinationChangedListener { _, destination, _ ->
                                when (destination.id) {
                                    R.id.authScreen,
                                    R.id.forgotPasswordScreen,
                                    R.id.feedsListScreen,
                                    R.id.feedCommentsScreen,
                                    R.id.createFeedScreen,
                                    R.id.legalScreen,
                                    R.id.feedViewScreen,
                                    R.id.chatScreen -> vm.hideBottomBar()
                                    else -> vm.showBottomBar()
                                }
                            }
                        }.also { controller -> navController = controller }
                    }
                })
                container
            }
        )
    }
}
