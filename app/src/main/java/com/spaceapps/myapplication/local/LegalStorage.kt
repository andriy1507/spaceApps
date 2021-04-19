package com.spaceapps.myapplication.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.spaceapps.myapplication.BuildConfig
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LegalStorage @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    companion object {
        private val TERMS_OF_USE_KEY =
            stringPreferencesKey("${BuildConfig.APPLICATION_ID}.TERMS_OF_USE")
        private val PRIVACY_POLICY_KEY =
            stringPreferencesKey("${BuildConfig.APPLICATION_ID}.PRIVACY_POLICY")
    }

    suspend fun saveTermsOfUse(content: String) = dataStore.edit {
        it[TERMS_OF_USE_KEY] = content
    }

    suspend fun savePrivacyPolicy(content: String) = dataStore.edit {
        it[PRIVACY_POLICY_KEY] = content
    }

    suspend fun getTermsOfUse() = dataStore.data.first()[TERMS_OF_USE_KEY]

    suspend fun getPrivacyPolicy() = dataStore.data.first()[PRIVACY_POLICY_KEY]
}
