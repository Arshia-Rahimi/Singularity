package com.github.singularity.core.broadcast

import com.appstractive.dnssd.DiscoveryEvent
import com.appstractive.dnssd.discoverServices
import com.github.singularity.core.shared.DISCOVER_TIMEOUT
import com.github.singularity.core.shared.model.JoinedSyncGroup
import com.github.singularity.core.shared.model.LocalServer
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.withTimeoutOrNull

class DeviceDiscoveryServiceImpl : DeviceDiscoveryService {

    override fun discoveredServers() = discoverServices(MDNS_SERVICE_TYPE)
        .runningFold(emptyList<LocalServer>()) { list, newServer ->
            when (newServer) {
                is DiscoveryEvent.Discovered -> {
                    newServer.resolve()
                    list
                }

                is DiscoveryEvent.Removed -> list - newServer.service.toServer()
                is DiscoveryEvent.Resolved -> list + newServer.service.toServer()
            }.distinctBy { it.syncGroupId }
        }

    override suspend fun discoverServer(syncGroup: JoinedSyncGroup) =
        withTimeoutOrNull(DISCOVER_TIMEOUT) {
            discoverServices(MDNS_SERVICE_TYPE).mapNotNull { newServer ->
                when (newServer) {
                    is DiscoveryEvent.Discovered -> newServer.resolve()
                    is DiscoveryEvent.Removed -> Unit

                    is DiscoveryEvent.Resolved -> {
                        val server = newServer.service.toServer()
                        if (server.syncGroupId == syncGroup.syncGroupId) {
                            return@mapNotNull server
                        }
                    }
                }
                return@mapNotNull null
            }.first()
        }

}
