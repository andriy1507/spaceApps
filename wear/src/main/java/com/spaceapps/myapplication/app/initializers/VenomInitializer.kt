package com.spaceapps.myapplication.app.initializers

import android.content.Context
import androidx.startup.Initializer
import com.github.venom.Venom

class VenomInitializer : Initializer<Venom> {

    override fun create(context: Context) = Venom.createInstance(context).also {
        it.initialize()
        it.start()
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
