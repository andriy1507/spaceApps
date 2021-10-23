package com.spaceapps.myapplication.app.repositories.devices

import androidx.paging.Pager
import com.spaceapps.myapplication.app.models.local.DeviceEntity

interface DevicesRepository {
    fun getDevices(): Pager<Int, DeviceEntity>
}
