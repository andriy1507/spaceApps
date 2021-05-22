package com.spaceapps.myapplication.app

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.spaceapps.myapplication.Settings
import com.spaceapps.myapplication.di.DataStoreModule.settingsDataStore
import com.spaceapps.myapplication.local.AuthTokenStorage
import com.spaceapps.myapplication.local.StorageManager
import com.spaceapps.myapplication.utils.AuthDispatcher
import com.spaceapps.myapplication.utils.NavigationDispatcher
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var authDispatcher: AuthDispatcher

    @Inject
    lateinit var authTokenStorage: AuthTokenStorage

    @Inject
    lateinit var navDispatcher: NavigationDispatcher

    @Inject
    lateinit var storageManager: StorageManager

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
        setupEdgeToEdge()
        setContent {
            MainScreen()
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
        storageManager.clear()
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
