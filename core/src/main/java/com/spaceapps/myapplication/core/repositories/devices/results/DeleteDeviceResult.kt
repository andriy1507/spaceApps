package com.spaceapps.myapplication.core.repositories.devices.results

sealed class DeleteDeviceResult {
    object Success : DeleteDeviceResult()
    object Failure : DeleteDeviceResult()
}
