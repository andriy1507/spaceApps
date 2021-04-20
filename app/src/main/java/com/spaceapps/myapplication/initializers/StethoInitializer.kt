package com.spaceapps.myapplication.initializers

import android.content.Context
import androidx.startup.Initializer
import com.facebook.stetho.Stetho

class StethoInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        Stetho.initializeWithDefaults(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
