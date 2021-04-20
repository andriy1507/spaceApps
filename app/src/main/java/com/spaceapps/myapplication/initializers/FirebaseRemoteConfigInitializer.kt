package com.spaceapps.myapplication.initializers

import android.content.Context
import androidx.startup.Initializer
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.spaceapps.myapplication.R

class FirebaseRemoteConfigInitializer : Initializer<FirebaseRemoteConfig> {
    override fun create(context: Context): FirebaseRemoteConfig {
        val remoteConfig = Firebase.remoteConfig(FirebaseApp.getInstance())
        val settings = FirebaseRemoteConfigSettings.Builder()
            .build()
        remoteConfig.setConfigSettingsAsync(settings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        return remoteConfig
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(FirebaseAppInitializer::class.java)
    }
}
