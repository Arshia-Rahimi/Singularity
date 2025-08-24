package com.github.singularity.core.discover.impl

import com.appstractive.dnssd.DiscoveryEvent
import com.appstractive.dnssd.discoverServices
import com.github.singularity.core.discover.DeviceDiscoveryService
import com.github.singularity.core.discover.MDNS_SERVICE_TYPE
import com.github.singularity.core.discover.Server
import com.github.singularity.core.discover.toServer
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.runningFold

class MdnsDeviceDiscoveryService : DeviceDiscoveryService {

    override fun discoverServers() = discoverServices(MDNS_SERVICE_TYPE)
        .runningFold(mutableListOf<Server>()) { list, newServer ->
            when (newServer) {
                is DiscoveryEvent.Discovered -> newServer.resolve()
                is DiscoveryEvent.Resolved -> list + newServer.service.toServer()

                is DiscoveryEvent.Removed -> list.removeAll { item ->
                    item.deviceId == newServer.service.toServer().deviceId
                }
            }
            return@runningFold list
        }.distinctUntilChanged()

}
