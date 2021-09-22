package com.spaceapps.myapplication.app.di

import android.content.Context
import androidx.room.Room
import com.spaceapps.myapplication.app.DATABASE_NAME
import com.spaceapps.myapplication.app.local.SpaceAppsDatabase
import com.spaceapps.myapplication.app.local.dao.LocationsDao
import com.spaceapps.myapplication.app.local.dao.LocationsRemoteKeyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideRoom(@ApplicationContext context: Context): SpaceAppsDatabase {
        val executor = Dispatchers.IO.asExecutor()
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
    fun provideLocationsRemoteKeyDao(db: SpaceAppsDatabase): LocationsRemoteKeyDao =
        db.getLocationsRemoteKeyDao()
}
