package com.spaceapps.myapplication.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.spaceapps.myapplication.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferencesModule {

    private const val SHARED_PREFS_NAME = "${BuildConfig.APPLICATION_ID}.SHARED_PREFS"

    private fun provideMasterKey(@ApplicationContext context: Context): MasterKey {
        return MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return if (BuildConfig.DEBUG)
        context.getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE)
        else EncryptedSharedPreferences.create(
            context,
            SHARED_PREFS_NAME,
            provideMasterKey(context),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

}