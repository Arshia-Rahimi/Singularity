package com.github.singularity.core.data.impl

import com.github.singularity.core.data.DeviceDiscoveryRepository
import com.github.singularity.core.mdns.MdnsDeviceDiscoveryService

class MdnsDeviceDiscoveryRepository(
    private val mdnsService: MdnsDeviceDiscoveryService
) : DeviceDiscoveryRepository {

    override val devices = mdnsService.devices

    override fun release() {
        mdnsService.release()
    }

}
