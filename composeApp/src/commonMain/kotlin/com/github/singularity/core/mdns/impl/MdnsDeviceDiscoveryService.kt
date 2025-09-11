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
        .runningFold(emptyList<LocalServer>()) { list, newServer ->
            when (newServer) {
                is DiscoveryEvent.Discovered -> {
                    newServer.resolve()
                    list
                }

                is DiscoveryEvent.Removed -> list.filter {
                    it.syncGroupId == newServer.service.toServer().syncGroupId
                }

                is DiscoveryEvent.Resolved -> list + newServer.service.toServer()
            }.distinctBy { it.syncGroupId }
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
