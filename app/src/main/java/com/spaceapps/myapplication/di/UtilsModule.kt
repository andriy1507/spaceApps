package com.spaceapps.myapplication.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.spaceapps.myapplication.utils.MoshiConverters
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilsModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder().add(MoshiConverters())
        .build()

    @Provides
    @Singleton
    fun provideLocationProviderClient(
        @ApplicationContext context: Context
    ) = FusedLocationProviderClient(context)
}