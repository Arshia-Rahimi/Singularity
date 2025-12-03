package com.github.singularity.core.datasource.network.impl

import com.bfo.zeroconf.Service
import com.github.singularity.core.datasource.network.LocalServerModel
import java.net.Inet4Address

sealed interface MdnsEvent {
	val server: LocalServerModel

	data class Resolved(override val server: LocalServerModel) : MdnsEvent
	data class Removed(override val server: LocalServerModel) : MdnsEvent
}


fun Service.toServer() = LocalServerModel(
	ip = addresses.firstOrNull {
		it is Inet4Address && !it.isLoopbackAddress
	}?.hostAddress ?: "Unknown",
	deviceOs = text["deviceOs"] ?: "Unknown",
	deviceName = text["deviceName"] ?: "Unknown",
	deviceId = text["deviceId"] ?: "Unknown",
	syncGroupName = text["syncGroupName"] ?: "Unknown",
	syncGroupId = text["syncGroupId"] ?: "Unknown",
)
