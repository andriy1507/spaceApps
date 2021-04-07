package com.spaceapps.myapplication.network

import com.spaceapps.myapplication.LANGUAGE_HEADER
import com.spaceapps.myapplication.Settings
import com.spaceapps.myapplication.local.SettingsStorage
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LanguageInterceptor @Inject constructor(
    private val settingsStorage: SettingsStorage
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        val language = getLanguage()
        language ?: return chain.proceed(builder.build())
        builder.header(LANGUAGE_HEADER, language)
        return chain.proceed(builder.build())
    }

    private fun getLanguage() = runBlocking {
        when (settingsStorage.getLanguage()) {
            Settings.Language.English -> "en"
            Settings.Language.Ukrainian -> "uk-UA"
            Settings.Language.UNRECOGNIZED -> null
        }
    }
}
