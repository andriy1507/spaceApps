package com.spaceapps.myapplication.core.utils

import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

class FirebaseProviderImpl : FirebaseProvider {
    override suspend fun getFirebaseMessagingToken(): String =
        FirebaseMessaging.getInstance().token.await()

    override suspend fun getFirebaseInstallationId(): String =
        FirebaseInstallations.getInstance().id.await()
}
