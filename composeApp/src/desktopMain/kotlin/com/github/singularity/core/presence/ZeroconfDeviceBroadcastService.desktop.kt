package com.github.singularity.core.presence

import com.bfo.zeroconf.Service
import com.bfo.zeroconf.Zeroconf
import com.github.singularity.core.data.PreferencesRepository
import com.github.singularity.core.shared.SERVER_PORT
import com.github.singularity.core.shared.deviceName
import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.core.shared.os
import com.github.singularity.core.shared.platform
import kotlinx.coroutines.flow.first

class ZeroconfDeviceBroadcastService(
	private val preferencesRepo: PreferencesRepository,
) : DeviceBroadcastService {

	private var zeroconf: Zeroconf? = null
	private var service: Service? = null

	override suspend fun startBroadcast(group: HostedSyncGroup) {
		val deviceId = preferencesRepo.preferences.first().deviceId

		zeroconf = Zeroconf()
		service = Service.Builder()
			.setName(getServiceName(group))
			.setType(MDNS_SERVICE_TYPE)
			.setPort(SERVER_PORT)
			.put("deviceName", deviceName)
			.put("deviceId", deviceId)
			.put("devicePlatform", platform)
			.put("deviceOs", os)
			.put("syncGroupName", group.name)
			.put("syncGroupId", group.hostedSyncGroupId)
			.build(zeroconf)

		service?.announce()
	}

	override suspend fun stopBroadcast() {
		service?.cancel()
		zeroconf?.close()
	}

}
