package com.spaceapps.utils

import android.os.Build
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import com.spaceapps.myapplication.core.utils.DeviceInfoProvider
import kotlinx.coroutines.tasks.await

class DeviceInfoProviderImpl : DeviceInfoProvider {
    override suspend fun getFirebaseMessagingToken(): String =
        FirebaseMessaging.getInstance().token.await()

    override suspend fun getFirebaseInstallationId(): String =
        FirebaseInstallations.getInstance().id.await()

    override fun provideManufacturer(): String = Build.MANUFACTURER

    override fun provideModel(): String = Build.MODEL

    override fun provideOsVersion(): String = "${Build.VERSION.SDK_INT}"
}
