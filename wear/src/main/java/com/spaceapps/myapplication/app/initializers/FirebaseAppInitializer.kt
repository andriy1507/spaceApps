package com.spaceapps.myapplication.app.initializers

import android.content.Context
import androidx.startup.Initializer
import com.google.firebase.FirebaseApp

class FirebaseAppInitializer : Initializer<FirebaseApp> {
    override fun create(context: Context): FirebaseApp {
        return FirebaseApp.initializeApp(context)
            ?: throw Exception("Failed to initialize Firebase application")
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
