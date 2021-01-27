package com.spaceapps.myapplication

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.spaceapps.myapplication.local.AuthTokenStorage
import com.spaceapps.myapplication.utils.NavDispatcher
import com.spaceapps.myapplication.utils.observeKey
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(
//    R.layout.activity_main
) {

    @Inject
    lateinit var sp: SharedPreferences

    @Inject
    lateinit var authTokenStorage: AuthTokenStorage

    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MainActivityScreen() }
//        val fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
//        if (fragment is NavHostFragment) {
//            navController = fragment.navController
//        }
        lifecycleScope.launchWhenCreated { observeAuthState() }
        lifecycleScope.launchWhenResumed { initNavigation() }
    }

    private suspend fun observeAuthState() {
        sp.observeKey(AuthTokenStorage.AUTH_TOKEN).collect {
            when (it) {
                "" -> unauthorize()
            }
        }
    }

    private fun unauthorize() {
        authTokenStorage.removeTokens()
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

    @Composable
    fun MainActivityScreen() = AndroidView({
        FragmentContainerView(it).apply {
            id = R.id.nav_host_fragment
        }
    }) {
        val navHostFragment = NavHostFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.nav_host_fragment, navHostFragment)
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