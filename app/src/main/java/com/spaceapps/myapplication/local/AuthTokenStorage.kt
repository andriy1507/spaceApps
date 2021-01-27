package com.spaceapps.myapplication.local

import android.content.SharedPreferences
import com.spaceapps.myapplication.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthTokenStorage @Inject constructor(private val prefs: SharedPreferences) {

    companion object {
        const val AUTH_TOKEN = "${BuildConfig.APPLICATION_ID}.AUTH_TOKEN"
        private const val REFRESH_TOKEN = "${BuildConfig.APPLICATION_ID}.REFRESH_TOKEN"
        private const val FCM_TOKEN = "${BuildConfig.APPLICATION_ID}.FCM_TOKEN"
    }

    val authToken: String?
        get() = prefs.getString(AUTH_TOKEN, null)

    val refreshToken: String?
        get() = prefs.getString(REFRESH_TOKEN, null)

    val fcmToken: String?
        get() = prefs.getString(FCM_TOKEN, null)

    fun storeTokens(authToken: String, refreshToken: String) =
        prefs.edit().putString(AUTH_TOKEN, authToken)
            .putString(REFRESH_TOKEN, refreshToken)
            .apply()

    fun storeFcmToken(token: String) = prefs.edit().putString(FCM_TOKEN, token).apply()

    fun removeTokens() = prefs.edit().remove(AUTH_TOKEN).remove(REFRESH_TOKEN).apply()

}