package com.spaceapps.myapplication.core.di

import android.content.Context
import androidx.room.Room
import com.spaceapps.myapplication.core.DATABASE_NAME
import com.spaceapps.myapplication.core.local.SpaceAppsDatabase
import com.spaceapps.myapplication.core.local.dao.*
import com.spaceapps.myapplication.core.utils.DispatchersProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.asExecutor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideRoom(
        @ApplicationContext context: Context,
        dispatchersProvider: DispatchersProvider
    ): SpaceAppsDatabase {
        val executor = dispatchersProvider.io.asExecutor()
        return Room
            .databaseBuilder(context, SpaceAppsDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .setTransactionExecutor(executor)
            .setQueryExecutor(executor)
            .build()
    }

    @Provides
    @Singleton
    fun provideLocationsDao(db: SpaceAppsDatabase): LocationsDao = db.getLocationsDao()

    @Provides
    @Singleton
    fun provideLocationsRemoteKeyDao(db: SpaceAppsDatabase): LocationsRemoteKeysDao =
        db.getLocationsRemoteKeyDao()

    @Provides
    @Singleton
    fun provideNotificationsDao(db: SpaceAppsDatabase): NotificationsDao = db.getNotificationsDao()

    @Provides
    @Singleton
    fun provideNotificationsRemoteKeyDao(db: SpaceAppsDatabase): NotificationsRemoteKeysDao =
        db.getNotificationsRemoteKeyDao()

    @Provides
    @Singleton
    fun provideDevicesDao(db: SpaceAppsDatabase): DevicesDao = db.getDevicesDao()

    @Provides
    @Singleton
    fun provideDevicesRemoteKeyDao(db: SpaceAppsDatabase): DevicesRemoteKeysDao =
        db.getDevicesRemoteKeysDao()
}
