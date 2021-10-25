package com.spaceapps.myapplication.app.repositories.devices

import androidx.paging.Pager
import com.spaceapps.myapplication.app.models.local.DeviceEntity
import com.spaceapps.myapplication.app.repositories.devices.results.DeleteDeviceResult

interface DevicesRepository {
    fun getDevices(): Pager<Int, DeviceEntity>
    suspend fun deleteDevice(id: Int): DeleteDeviceResult
}
