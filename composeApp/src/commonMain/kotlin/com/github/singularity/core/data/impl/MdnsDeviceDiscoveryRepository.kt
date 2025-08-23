package com.github.singularity.core.data.impl

import com.github.singularity.core.data.DeviceDiscoveryRepository
import com.github.singularity.core.mdns.DeviceDiscoveryService

class MdnsDeviceDiscoveryRepository(
    private val mdnsService: DeviceDiscoveryService,
) : DeviceDiscoveryRepository {

    override val devices = mdnsService.devices

    override fun release() {
        mdnsService.release()
    }

}
