package com.spaceapps.myapplication.app.repositories.devices.results

sealed class DeleteDeviceResult {
    object Success : DeleteDeviceResult()
    object Failure : DeleteDeviceResult()
}
