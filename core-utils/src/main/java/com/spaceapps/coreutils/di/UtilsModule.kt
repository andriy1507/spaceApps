package com.spaceapps.coreutils.di

import com.spaceapps.myapplication.core.utils.AuthDispatcher
import com.spaceapps.myapplication.core.utils.DeviceInfoProvider
import com.spaceapps.myapplication.core.utils.DispatchersProvider
import com.spaceapps.coreutils.AuthDispatcherImpl
import com.spaceapps.coreutils.DeviceInfoProviderImpl
import com.spaceapps.coreutils.DispatchersProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilsModule {

    @Provides
    @Singleton
    fun provideAuthDispatcher(): AuthDispatcher = AuthDispatcherImpl()

    @Provides
    @Singleton
    fun provideDispatchersProvider(): DispatchersProvider = DispatchersProviderImpl()

    @Provides
    @Singleton
    fun provideDeviceInfoProvider(): DeviceInfoProvider = DeviceInfoProviderImpl()
}
