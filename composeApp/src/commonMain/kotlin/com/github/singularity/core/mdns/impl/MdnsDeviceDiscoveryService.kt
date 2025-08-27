package com.github.singularity.core.mdns.impl

import com.appstractive.dnssd.DiscoveryEvent
import com.appstractive.dnssd.discoverServices
import com.github.singularity.core.database.entities.JoinedSyncGroup
import com.github.singularity.core.mdns.DeviceDiscoveryService
import com.github.singularity.core.mdns.MDNS_SERVICE_TYPE
import com.github.singularity.core.mdns.Server
import com.github.singularity.core.mdns.toServer
import com.github.singularity.core.shared.util.asResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
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
        }
        .map { it.toList() }
        .distinctUntilChanged()

    override fun discoverServer(syncGroup: JoinedSyncGroup) = flow {
        discoverServices(MDNS_SERVICE_TYPE).collect { newServer ->
            when (newServer) {
                is DiscoveryEvent.Discovered -> newServer.resolve()
                is DiscoveryEvent.Resolved -> {
                    val server = newServer.service.toServer()
                    if(server.syncGroupId == syncGroup.joinedSyncGroupId) {
                        emit(server)
                    }
                }
                else -> Unit
            }
        }
    }.asResult(Dispatchers.IO)

}
