package com.github.singularity.core.mdns.impl

import com.appstractive.dnssd.DiscoveryEvent
import com.appstractive.dnssd.discoverServices
import com.github.singularity.core.database.entities.JoinedSyncGroup
import com.github.singularity.core.mdns.DeviceDiscoveryService
import com.github.singularity.core.mdns.MDNS_SERVICE_TYPE
import com.github.singularity.core.mdns.toServer
import com.github.singularity.core.shared.model.LocalServer
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.withTimeoutOrNull

class MdnsDeviceDiscoveryService : DeviceDiscoveryService {

    override fun discoverServers() = discoverServices(MDNS_SERVICE_TYPE)
        .runningFold(mutableListOf<LocalServer>()) { list, newServer ->
            when (newServer) {
                is DiscoveryEvent.Discovered -> newServer.resolve()
                is DiscoveryEvent.Resolved -> list + newServer.service.toServer()

                is DiscoveryEvent.Removed -> list.removeAll { item ->
                    item.deviceId == newServer.service.toServer().deviceId
                }
            }
            return@runningFold list
        }
        .map { it.toList() }
        .distinctUntilChanged()

    override suspend fun discoverServer(syncGroup: JoinedSyncGroup) = withTimeoutOrNull(30000) {
        discoverServices(MDNS_SERVICE_TYPE).mapNotNull { newServer ->
            when (newServer) {
                is DiscoveryEvent.Discovered -> {
                    newServer.resolve()
                    null
                }
                is DiscoveryEvent.Resolved -> {
                    val server = newServer.service.toServer()
                    if(server.syncGroupId == syncGroup.joinedSyncGroupId) {
                        server
                    } else null
                }

                is DiscoveryEvent.Removed -> null
            }
        }.first()
    }

}
