package com.spaceapps.myapplication.app.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.spaceapps.myapplication.app.Navigation
import com.spaceapps.myapplication.app.local.DataStoreManager
import com.spaceapps.myapplication.app.local.SpaceAppsDatabase
import com.spaceapps.myapplication.utils.AuthDispatcher
import com.spaceapps.myapplication.utils.NavigationDispatcher
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            LaunchedEffect(navController) {
                navDispatcher.emitter.onEach { command ->
                    command(navController)
                }.launchIn(this)
            }
            Scaffold {
                PopulatedNavHost(navController, Navigation.geolocation)
            }
        }
        observeAuthState()
    }

    private fun observeAuthState() = authDispatcher.emitter.onEach {
        when (it) {
            true -> logOut()
            false -> restart()
        }
    }.launchIn(lifecycleScope)

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
