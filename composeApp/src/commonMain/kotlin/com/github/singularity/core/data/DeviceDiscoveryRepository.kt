package com.github.singularity.core.data

import com.github.singularity.core.mdns.Device
import kotlinx.coroutines.flow.MutableStateFlow

interface DeviceDiscoveryRepository {

    val devices: MutableStateFlow<List<Device>>

    fun release()

}
