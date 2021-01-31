package com.spaceapps.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.spaceapps.myapplication.local.AuthTokenStorage
import com.spaceapps.myapplication.utils.NavDispatcher
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var authTokenStorage: AuthTokenStorage

    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupEdgeToEdge()
        setTheme(R.style.Theme_MyApplication_NoActionBar)
        setContent { MainActivityScreen() }
//        lifecycleScope.launchWhenCreated { observeAuthState() }
        lifecycleScope.launchWhenResumed { initNavigation() }
    }

//    private fun observeAuthState() {
//    }

//    private fun unauthorize() = lifecycleScope.launch {
//        authTokenStorage.removeTokens()
//        restart()
//    }

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
    fun MainActivityScreen() = AndroidView(
        modifier = Modifier.testTag("navHostFragment"),
        viewBlock = {
            FragmentContainerView(it).apply { id = R.id.navHostFragment }
        }
    ) {
        val navHostFragment = NavHostFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.navHostFragment, navHostFragment)
            .commit()
        navHostFragment.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                navHostFragment.navController.apply { setGraph(R.navigation.nav_graph) }.also {
                    navController = it
                }
            }
        })
    }
}
