package com.github.singularity.core.datasource.network.impl

import com.appstractive.dnssd.DiscoveryEvent
import com.appstractive.dnssd.discoverServices
import com.github.singularity.core.datasource.database.JoinedSyncGroupModel
import com.github.singularity.core.datasource.network.DeviceDiscoveryService
import com.github.singularity.core.datasource.network.LocalServerModel
import com.github.singularity.core.datasource.network.MDNS_SERVICE_TYPE
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.runningFold

class DnsSdDeviceDiscoveryService : DeviceDiscoveryService {

    override fun discoverServers() = discoverServices(MDNS_SERVICE_TYPE)
	    .runningFold(emptyList<LocalServerModel>()) { list, newServer ->
            when (newServer) {
                is DiscoveryEvent.Discovered -> {
                    newServer.resolve()
                    list
                }

                is DiscoveryEvent.Removed -> list - newServer.service.toServer()
                is DiscoveryEvent.Resolved -> list + newServer.service.toServer()
            }.distinctBy { it.syncGroupId }
        }

	override suspend fun findServer(syncGroup: JoinedSyncGroupModel) =
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
