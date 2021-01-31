package com.spaceapps.myapplication.di

import android.content.Context
import androidx.datastore.preferences.createDataStore
import com.spaceapps.myapplication.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    private const val PREFERENCES_DATA_STORE = "${BuildConfig.APPLICATION_ID}.PREFS_DATA_STORE"

    @Provides
    @Singleton
    fun providePreferencesDataStore(@ApplicationContext context: Context) =
        context.createDataStore(PREFERENCES_DATA_STORE)
}
