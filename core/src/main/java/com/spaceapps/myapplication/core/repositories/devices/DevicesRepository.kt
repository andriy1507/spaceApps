package com.spaceapps.myapplication.core.repositories.devices

import androidx.paging.Pager
import com.spaceapps.myapplication.core.models.local.devices.DeviceEntity
import com.spaceapps.myapplication.core.repositories.devices.results.DeleteDeviceResult

interface DevicesRepository {
    fun getDevices(): Pager<Int, DeviceEntity>
    suspend fun deleteDevice(id: Int): DeleteDeviceResult
}
