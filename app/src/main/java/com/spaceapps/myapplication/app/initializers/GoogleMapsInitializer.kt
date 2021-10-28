package com.spaceapps.myapplication.app.initializers

import android.content.Context
import androidx.startup.Initializer
import com.google.android.gms.maps.MapsInitializer

class GoogleMapsInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        MapsInitializer.initialize(
            context.applicationContext,
            MapsInitializer.Renderer.LATEST,
            null
        )
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}
