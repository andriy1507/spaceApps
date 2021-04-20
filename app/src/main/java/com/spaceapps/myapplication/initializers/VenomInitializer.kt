package com.spaceapps.myapplication.initializers

import android.content.Context
import androidx.startup.Initializer
import com.github.venom.Venom

class VenomInitializer : Initializer<Venom> {
    override fun create(context: Context): Venom {
        val venom = Venom.createInstance(context)
        venom.initialize()
        venom.start()
        return venom
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
