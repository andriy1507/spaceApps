package com.spaceapps.myapplication.core.utils

interface DeviceInfoProvider {

    suspend fun getFirebaseMessagingToken(): String

    suspend fun getFirebaseInstallationId(): String

    fun provideManufacturer(): String

    fun provideModel(): String

    fun provideOsVersion(): String
}
