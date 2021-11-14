package com.spaceapps.myapplication.core.repositories.auth.results

sealed class AddDeviceResult {
    object Success : AddDeviceResult()
    object Failure : AddDeviceResult()
}