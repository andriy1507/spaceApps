package com.spaceapps.myapplication.core.di

import com.spaceapps.myapplication.core.repositories.auth.AuthRepository
import com.spaceapps.myapplication.core.repositories.auth.AuthRepositoryImpl
import com.spaceapps.myapplication.core.repositories.devices.DevicesRepository
import com.spaceapps.myapplication.core.repositories.devices.DevicesRepositoryImpl
import com.spaceapps.myapplication.core.repositories.locations.LocationsRepository
import com.spaceapps.myapplication.core.repositories.locations.LocationsRepositoryImpl
import com.spaceapps.myapplication.core.repositories.notifications.NotificationsRepository
import com.spaceapps.myapplication.core.repositories.notifications.NotificationsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    fun bindLocationsRepository(impl: LocationsRepositoryImpl): LocationsRepository

    @Binds
    fun bindNotificationsRepository(impl: NotificationsRepositoryImpl): NotificationsRepository

    @Binds
    fun bindDevicesRepository(impl: DevicesRepositoryImpl): DevicesRepository
}
