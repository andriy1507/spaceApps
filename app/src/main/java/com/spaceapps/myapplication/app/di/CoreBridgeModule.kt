package com.spaceapps.myapplication.app.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.spaceapps.myapplication.core.di.CoreComponent
import com.spaceapps.myapplication.core.di.CoreEntryPoint
import com.spaceapps.myapplication.core.local.DataStoreManager
import com.spaceapps.myapplication.core.local.DatabaseManager
import com.spaceapps.myapplication.core.repositories.auth.AuthRepository
import com.spaceapps.myapplication.core.repositories.devices.DevicesRepository
import com.spaceapps.myapplication.core.repositories.files.FilesRepository
import com.spaceapps.myapplication.core.repositories.locations.LocationsRepository
import com.spaceapps.myapplication.core.repositories.notifications.NotificationsRepository
import com.spaceapps.myapplication.core.repositories.signalr.SignalrRepository
import com.spaceapps.myapplication.core.utils.DispatchersProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreBridgeModule {

    @Provides
    @Singleton
    fun provideCoreEntryPoint(@ApplicationContext context: Context): CoreEntryPoint {
        return CoreComponent.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(entryPoint: CoreEntryPoint): AuthRepository =
        entryPoint.provideAuthRepository()

    @Provides
    @Singleton
    fun provideLocationsRepository(entryPoint: CoreEntryPoint): LocationsRepository =
        entryPoint.provideLocationsRepository()

    @Provides
    @Singleton
    fun provideNotificationsRepository(entryPoint: CoreEntryPoint): NotificationsRepository =
        entryPoint.provideNotificationsRepository()

    @Provides
    @Singleton
    fun provideDevicesRepository(entryPoint: CoreEntryPoint): DevicesRepository =
        entryPoint.provideDevicesRepository()

    @Provides
    @Singleton
    fun provideSignalrRepository(entryPoint: CoreEntryPoint): SignalrRepository =
        entryPoint.provideSignalrRepository()

    @Provides
    @Singleton
    fun provideFilesRepository(entryPoint: CoreEntryPoint): FilesRepository =
        entryPoint.provideFilesRepository()

    @Provides
    @Singleton
    fun provideLocationProviderClient(entryPoint: CoreEntryPoint): FusedLocationProviderClient =
        entryPoint.provideLocationProviderClient()

    @Provides
    @Singleton
    fun provideDispatchersProvider(entryPoint: CoreEntryPoint): DispatchersProvider =
        entryPoint.provideDispatchersProvider()

    @Provides
    @Singleton
    fun provideDatabaseManager(entryPoint: CoreEntryPoint): DatabaseManager =
        entryPoint.provideDatabaseManager()

    @Provides
    @Singleton
    fun provideDataStoreManager(entryPoint: CoreEntryPoint): DataStoreManager =
        entryPoint.provideDataStoreManager()
}
