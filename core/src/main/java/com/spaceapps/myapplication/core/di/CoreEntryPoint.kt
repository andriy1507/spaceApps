package com.spaceapps.myapplication.core.di

import com.google.android.gms.location.FusedLocationProviderClient
import com.spaceapps.myapplication.core.local.StorageManager
import com.spaceapps.myapplication.core.repositories.auth.AuthRepository
import com.spaceapps.myapplication.core.repositories.auth.AuthRepositoryImpl
import com.spaceapps.myapplication.core.repositories.devices.DevicesRepository
import com.spaceapps.myapplication.core.repositories.devices.DevicesRepositoryImpl
import com.spaceapps.myapplication.core.repositories.locations.LocationsRepository
import com.spaceapps.myapplication.core.repositories.locations.LocationsRepositoryImpl
import com.spaceapps.myapplication.core.repositories.notifications.NotificationsRepository
import com.spaceapps.myapplication.core.repositories.notifications.NotificationsRepositoryImpl
import com.spaceapps.myapplication.core.repositories.signalr.SignalrRepository
import com.spaceapps.myapplication.core.repositories.signalr.SignalrRepositoryImpl
import com.spaceapps.myapplication.core.utils.DispatchersProvider
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn

@EntryPoint
@InstallIn(CoreComponent::class)
interface CoreEntryPoint {

    fun provideAuthRepository(): AuthRepository

    fun provideLocationsRepository(): LocationsRepository

    fun provideNotificationsRepository(): NotificationsRepository

    fun provideDevicesRepository(): DevicesRepository

    fun provideSignalrRepository(): SignalrRepository

    fun provideLocationProviderClient(): FusedLocationProviderClient

    fun provideDispatchersProvider(): DispatchersProvider

    fun provideStorageManager(): StorageManager
}