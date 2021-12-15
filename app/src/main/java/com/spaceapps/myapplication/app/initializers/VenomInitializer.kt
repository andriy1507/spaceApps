package com.spaceapps.myapplication.app.initializers

import android.content.Context
import androidx.startup.Initializer
import com.github.venom.Venom
import com.github.venom.service.NotificationConfig
import com.spaceapps.myapplication.R

class VenomInitializer : Initializer<Venom> {
    override fun create(context: Context): Venom {
        val venom = Venom.createInstance(context)
        val config = NotificationConfig.Builder(context)
            .color(R.color.colorPrimary)
            .icon(R.drawable.ic_launcher_foreground)
            .build()
        venom.initialize(config = config)
        venom.start()
        return venom
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
