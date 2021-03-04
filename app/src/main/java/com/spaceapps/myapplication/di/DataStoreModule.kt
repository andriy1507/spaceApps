package com.spaceapps.myapplication.di

import android.content.Context
import androidx.datastore.dataStore
import androidx.datastore.preferences.preferencesDataStore
import com.spaceapps.myapplication.BuildConfig
import com.spaceapps.myapplication.local.SettingsSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val PREFERENCES_DATA_STORE = "${BuildConfig.APPLICATION_ID}.PREFS_DATA_STORE"
private const val SETTINGS_DATA_STORE = "${BuildConfig.APPLICATION_ID}.SETTINGS_DATA_STORE"

private val Context.preferencesDataStore by preferencesDataStore(PREFERENCES_DATA_STORE)
val Context.settingsDataStore by dataStore(SETTINGS_DATA_STORE, SettingsSerializer)

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun providePreferencesDataStore(@ApplicationContext context: Context) =
        context.preferencesDataStore

    @Provides
    @Singleton
    fun provideSettingsDataStore(@ApplicationContext context: Context) = context.settingsDataStore
}
