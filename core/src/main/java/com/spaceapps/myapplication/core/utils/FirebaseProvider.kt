package com.spaceapps.myapplication.core.utils

interface FirebaseProvider {

    suspend fun getFirebaseMessagingToken(): String

    suspend fun getFirebaseInstallationId(): String
}
