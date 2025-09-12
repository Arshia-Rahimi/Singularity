package com.github.singularity.core.broadcast

import com.github.singularity.core.shared.model.JoinedSyncGroup
import com.github.singularity.core.shared.model.LocalServer
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.withTimeoutOrNull
import javax.jmdns.JmDNS
import javax.jmdns.ServiceEvent
import javax.jmdns.ServiceListener

class JmdnsDeviceDiscoveryService(
    private val jmdns: JmDNS,
) : DeviceDiscoveryService {

    private val servers = callbackFlow {
        val listener = object : ServiceListener {
            override fun serviceAdded(event: ServiceEvent?) {
                event?.info
            }

            override fun serviceRemoved(event: ServiceEvent?) {
                event?.info?.toServer()?.let {
                    trySend(JmdnsEvent.Removed(it))
                }
            }

            override fun serviceResolved(event: ServiceEvent?) {
                event?.info?.toServer()?.let {
                    trySend(JmdnsEvent.Resolved(it))
                }
            }
        }

        jmdns.addServiceListener(MDNS_SERVICE_TYPE, listener)
        awaitClose {
            jmdns.removeServiceListener(MDNS_SERVICE_TYPE, listener)
        }

    }

    override val discoveredServers = servers
        .runningFold(emptyList<LocalServer>()) { list, newServer ->
            when (newServer) {
                is JmdnsEvent.Removed -> list - newServer.server
                is JmdnsEvent.Resolved -> list + newServer.server
            }
            list
        }

    override suspend fun discoverServer(syncGroup: JoinedSyncGroup) =
        withTimeoutOrNull(30_000) {
            servers.filterIsInstance<JmdnsEvent.Resolved>()
                .map { it.server }
                .firstOrNull { it.syncGroupId == syncGroup.syncGroupId }
        }

}
