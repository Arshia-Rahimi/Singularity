package com.github.singularity.core.datasource.network.presence

//import com.github.singularity.core.shared.model.JoinedSyncGroup
//import com.github.singularity.core.shared.model.LocalServer
//import kotlinx.coroutines.channels.awaitClose
//import kotlinx.coroutines.flow.callbackFlow
//import kotlinx.coroutines.flow.filterIsInstance
//import kotlinx.coroutines.flow.firstOrNull
//import kotlinx.coroutines.flow.map
//import kotlinx.coroutines.flow.runningFold
//import javax.jmdns.ServiceEvent
//import javax.jmdns.ServiceListener
//
//class JmdnsDeviceDiscoveryService : DeviceDiscoveryService {
//
//    private val serviceType = MDNS_SERVICE_TYPE
//
//    private val servers = callbackFlow {
//        val listener = object : ServiceListener {
//            override fun serviceAdded(event: ServiceEvent?) {
//                event?.info
//            }
//
//            override fun serviceRemoved(event: ServiceEvent?) {
//                event?.info?.toServer()?.let {
//                    trySend(MdnsEvent.Removed(it))
//                }
//            }
//
//            override fun serviceResolved(event: ServiceEvent?) {
//                event?.info?.toServer()?.let {
//                    trySend(MdnsEvent.Resolved(it))
//                }
//            }
//        }
//
//        val jmdns = getJmdns()
//        jmdns.addServiceListener(serviceType, listener)
//
//        awaitClose {
//            jmdns.removeServiceListener(serviceType, listener)
//        }
//    }
//
//    override fun discoveredServers() = servers
//        .runningFold(emptyList<LocalServer>()) { list, newServer ->
//            when (newServer) {
//                is MdnsEvent.Removed -> list - newServer.server
//                is MdnsEvent.Resolved -> list + newServer.server
//            }.distinctBy { it.syncGroupId }
//        }
//
//    override suspend fun discoverServer(syncGroup: JoinedSyncGroup) =
//        servers.filterIsInstance<MdnsEvent.Resolved>()
//            .map { it.server }
//            .firstOrNull { it.syncGroupId == syncGroup.syncGroupId }
//
//}
