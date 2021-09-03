package com.spaceapps.myapplication.app.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.spaceapps.myapplication.app.PREFERENCES_DATA_STORE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    private val Context.preferencesDataStore by preferencesDataStore(PREFERENCES_DATA_STORE)

    @Provides
    @Singleton
    fun providePreferencesDataStore(@ApplicationContext context: Context) =
        context.preferencesDataStore

}
