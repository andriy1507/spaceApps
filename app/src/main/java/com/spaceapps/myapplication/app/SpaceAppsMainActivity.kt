package com.spaceapps.myapplication.app

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.Settings
import com.spaceapps.myapplication.databinding.ActivityMainBinding
import com.spaceapps.myapplication.di.DataStoreModule.settingsDataStore
import com.spaceapps.myapplication.local.AuthTokenStorage
import com.spaceapps.myapplication.local.StorageManager
import com.spaceapps.myapplication.utils.AuthDispatcher
import com.spaceapps.myapplication.utils.NavDispatcher
import dagger.hilt.android.AndroidEntryPoint
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

    private lateinit var navController: NavController

    private lateinit var binding: ActivityMainBinding

    override fun attachBaseContext(newBase: Context) = runBlocking {
        val lang = when (newBase.settingsDataStore.data.first().language) {
            Settings.Language.Ukrainian -> "uk"
            else -> "en"
        }
        super.attachBaseContext(SpaceAppsContextWrapper.wrap(newBase, lang))
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration) = runBlocking {
        val lang = when (settingsDataStore.data.first().language) {
            Settings.Language.Ukrainian -> "uk"
            else -> "en"
        }
        overrideConfiguration.setLocale(Locale(lang))
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_MyApplication_NoActionBar)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge()
        initNavigation()
        lifecycleScope.launchWhenCreated { observeAuthState() }
        lifecycleScope.launchWhenResumed { initNavigation() }
    }

    private suspend fun observeAuthState() {
        for (e in authDispatcher.emitter) {
            if (e) logOut() else restart()
        }
    }

    private fun logOut() = lifecycleScope.launch(Dispatchers.IO) {
        storageManager.clear()
        restart()
    }

    private fun restart() {
        finish()
        startActivity(intent)
    }

    private fun initNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
        navGraph.startDestination = getStartDestination()
        navController.graph = navGraph
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.authScreen,
                R.id.qrCodeScreen,
                R.id.legalScreen,
                R.id.forgotPasswordScreen -> binding.bottomNavView.isGone = true
                else -> binding.bottomNavView.isVisible = true
            }
        }
        NavigationUI.setupWithNavController(binding.bottomNavView, navController)
        lifecycleScope.launchWhenResumed {
            for (event in navDispatcher.events) navController.event()
        }
    }

    private fun getStartDestination() =
        if (runBlocking { authTokenStorage.getAuthToken() }.isNullOrBlank()) {
            R.id.authScreen
        } else {
            R.id.geolocationScreen
        }

    private fun setupEdgeToEdge() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }
}
