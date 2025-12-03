package com.github.singularity.core.datasource.network.impl

import com.bfo.zeroconf.Service
import com.bfo.zeroconf.Zeroconf
import com.bfo.zeroconf.ZeroconfListener
import com.github.singularity.core.datasource.database.JoinedSyncGroupModel
import com.github.singularity.core.datasource.network.DeviceDiscoveryService
import com.github.singularity.core.datasource.network.LocalServerModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.runningFold

class ZeroconfDeviceDiscoveryService : DeviceDiscoveryService {

	private val servers = callbackFlow {
		val zeroconf = Zeroconf()
		zeroconf.addListener(object : ZeroconfListener {
			override fun serviceAnnounced(service: Service?) {
				super.serviceAnnounced(service)
				service?.toServer()?.let {
					trySend(MdnsEvent.Resolved(it))
				}
			}

			override fun serviceExpired(service: Service?) {
				super.serviceExpired(service)
				service?.toServer()?.let {
					trySend(MdnsEvent.Removed(it))
				}
			}

			override fun serviceNamed(type: String?, name: String?) {
				if ("_tcp" == type) {
					zeroconf.query(type, name)
				}
			}
		})

		awaitClose {
			zeroconf.close()
		}
	}

	override fun discoverServers(): Flow<List<LocalServerModel>> = servers
		.runningFold(emptyList()) { list, newServer ->
			when (newServer) {
				is MdnsEvent.Removed -> list - newServer.server
				is MdnsEvent.Resolved -> list + newServer.server
			}.distinctBy { it.syncGroupId }
		}

	override suspend fun findServer(syncGroup: JoinedSyncGroupModel): LocalServerModel? =
		servers.filterIsInstance<MdnsEvent.Resolved>()
			.map { it.server }
			.firstOrNull { it.syncGroupId == syncGroup.syncGroupId }

}
