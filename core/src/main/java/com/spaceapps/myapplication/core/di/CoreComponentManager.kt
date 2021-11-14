package com.spaceapps.myapplication.core.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.spaceapps.myapplication.core.local.StorageManager
import com.spaceapps.myapplication.core.repositories.auth.AuthRepository
import com.spaceapps.myapplication.core.repositories.devices.DevicesRepository
import com.spaceapps.myapplication.core.repositories.locations.LocationsRepository
import com.spaceapps.myapplication.core.repositories.notifications.NotificationsRepository
import com.spaceapps.myapplication.core.repositories.signalr.SignalrRepository
import com.spaceapps.myapplication.core.utils.DispatchersProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CoreComponentManager {

    @Provides
    @Singleton
    fun provideCoreEntryPoint(@ApplicationContext context: Context): CoreEntryPoint =
        EntryPointAccessors.fromApplication(context, CoreEntryPoint::class.java)

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
    fun provideLocationProviderClient(entryPoint: CoreEntryPoint): FusedLocationProviderClient =
        entryPoint.provideLocationProviderClient()

    @Provides
    @Singleton
    fun provideDispatchersProvider(entryPoint: CoreEntryPoint): DispatchersProvider =
        entryPoint.provideDispatchersProvider()

    @Provides
    @Singleton
    fun provideStorageManager(entryPoint: CoreEntryPoint): StorageManager =
        entryPoint.provideStorageManager()
}
