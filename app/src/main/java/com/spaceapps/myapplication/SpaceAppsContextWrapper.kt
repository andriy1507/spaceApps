package com.spaceapps.myapplication

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import androidx.core.os.ConfigurationCompat
import java.util.*

class SpaceAppsContextWrapper(baseContext: Context) : ContextWrapper(baseContext) {

    companion object {
        fun wrap(context: Context, language: String): SpaceAppsContextWrapper {
            val config = context.resources.configuration
            val sysLocale = getSystemLocale(config)
            if (language.isNotEmpty() && sysLocale.language != language) {
                val locale = Locale(language)
                Locale.setDefault(locale)
                setSystemLocale(config, locale)
            }
            return SpaceAppsContextWrapper(context.createConfigurationContext(config))
        }

        private fun getSystemLocale(config: Configuration) =
            ConfigurationCompat.getLocales(config).get(0)

        private fun setSystemLocale(config: Configuration, locale: Locale) =
            config.setLocale(locale)
    }
}
