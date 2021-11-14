package com.spaceapps.myapplication.core.di.modules

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.spaceapps.myapplication.core.di.CoreComponent
import com.spaceapps.myapplication.core.local.*
import com.spaceapps.myapplication.core.utils.DispatchersProvider
import com.spaceapps.myapplication.core.utils.DispatchersProviderImpl
import com.spaceapps.myapplication.core.utils.MoshiConverters
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(CoreComponent::class)
object UtilsModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder().add(MoshiConverters()).build()

    @Provides
    @Singleton
    fun provideLocationProviderClient(
        @ApplicationContext context: Context
    ): FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    @Provides
    @Singleton
    fun provideDispatchersProvider(): DispatchersProvider = DispatchersProviderImpl

    @Provides
    @Singleton
    fun provideDatabaseManager(db: SpaceAppsDatabase): DatabaseManager {
        return DatabaseManagerImpl(db)
    }

    @Provides
    @Singleton
    fun provideDataStoreManager(@ApplicationContext context: Context): DataStoreManager {
        return DataStoreManagerImpl(context)
    }
}
