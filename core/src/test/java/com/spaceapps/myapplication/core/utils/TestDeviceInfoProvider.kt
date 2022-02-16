package com.spaceapps.myapplication.core.utils

class TestDeviceInfoProvider : DeviceInfoProvider {
    override suspend fun getFirebaseMessagingToken() = ""

    override suspend fun getFirebaseInstallationId() = ""

    override fun provideManufacturer() = ""

    override fun provideModel() = ""

    override fun provideOsVersion() = ""
}
