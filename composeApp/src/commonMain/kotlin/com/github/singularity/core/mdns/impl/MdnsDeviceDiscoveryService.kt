package com.github.singularity.core.mdns.impl

import com.appstractive.dnssd.DiscoveryEvent
import com.appstractive.dnssd.discoverServices
import com.github.singularity.core.mdns.DeviceDiscoveryService
import com.github.singularity.core.mdns.MDNS_SERVICE_TYPE
import com.github.singularity.core.mdns.toServer
import com.github.singularity.core.shared.model.JoinedSyncGroup
import com.github.singularity.core.shared.model.LocalServer
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.withTimeoutOrNull

class MdnsDeviceDiscoveryService : DeviceDiscoveryService {

    override val discoveredServers = discoverServices(MDNS_SERVICE_TYPE)
        .runningFold(mutableListOf<LocalServer>()) { list, newServer ->
            val newList = list
            when (newServer) {
                is DiscoveryEvent.Discovered -> newServer.resolve()
                is DiscoveryEvent.Resolved -> newList += newServer.service.toServer()
                is DiscoveryEvent.Removed -> {
                    newList.removeAll { item ->
                        item.syncGroupId == newServer.service.toServer().syncGroupId
                    }
                }
            }
            newList.distinctBy { it.syncGroupId }
            newList
        }

    override suspend fun discoverServer(syncGroup: JoinedSyncGroup) = withTimeoutOrNull(30_000) {
        discoverServices(MDNS_SERVICE_TYPE).mapNotNull { newServer ->
            when (newServer) {
                is DiscoveryEvent.Discovered -> {
                    newServer.resolve()
                    null
                }

                is DiscoveryEvent.Resolved -> {
                    val server = newServer.service.toServer()
                    if (server.syncGroupId == syncGroup.syncGroupId) {
                        server
                    } else null
                }

                is DiscoveryEvent.Removed -> null
            }
        }.first()
    }

}
