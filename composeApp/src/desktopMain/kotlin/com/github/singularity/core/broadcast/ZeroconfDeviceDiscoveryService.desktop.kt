package com.github.singularity.core.broadcast

import com.bfo.zeroconf.Service
import com.bfo.zeroconf.Zeroconf
import com.bfo.zeroconf.ZeroconfListener
import com.github.singularity.core.shared.model.JoinedSyncGroup
import com.github.singularity.core.shared.model.LocalServer
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ZeroconfDeviceDiscoveryService: DeviceDiscoveryService {

	override fun discoveredServers(): Flow<List<LocalServer>> = callbackFlow {
		val zeroconf = Zeroconf()
		zeroconf.addListener(object: ZeroconfListener {
			override fun serviceAnnounced(service: Service?) {
				super.serviceAnnounced(service)

			}

			override fun serviceNamed(type: String?, name: String?) {
				if ("_tcp" == type) {
					zeroconf.query(type, name)
				}
			}
		});

		awaitClose {
			zeroconf.close()
		}
	}

	override suspend fun discoverServer(syncGroup: JoinedSyncGroup): LocalServer? {
		TODO()
	}

}
