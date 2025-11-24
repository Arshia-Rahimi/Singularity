package com.github.singularity.core.datasource.presence.impl

import com.appstractive.dnssd.DiscoveryEvent
import com.appstractive.dnssd.discoverServices
import com.github.singularity.core.datasource.presence.DeviceDiscoveryService
import com.github.singularity.core.datasource.presence.MDNS_SERVICE_TYPE
import com.github.singularity.core.shared.model.JoinedSyncGroup
import com.github.singularity.core.shared.model.LocalServer
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.runningFold

class DnsSdDeviceDiscoveryService : DeviceDiscoveryService {

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
