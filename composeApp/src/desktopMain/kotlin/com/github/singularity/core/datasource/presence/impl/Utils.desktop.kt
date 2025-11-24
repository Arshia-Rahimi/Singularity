package com.github.singularity.core.datasource.presence.impl

import com.bfo.zeroconf.Service
import com.github.singularity.core.shared.model.LocalServer
import java.net.Inet4Address

sealed interface MdnsEvent {
	val server: LocalServer

	data class Resolved(override val server: LocalServer) : MdnsEvent
	data class Removed(override val server: LocalServer) : MdnsEvent
}


fun Service.toServer() = LocalServer(
	ip = addresses.firstOrNull {
		it is Inet4Address && !it.isLoopbackAddress
	}?.hostAddress ?: "Unknown",
	deviceOs = text["deviceOs"] ?: "Unknown",
	deviceName = text["deviceName"] ?: "Unknown",
	deviceId = text["deviceId"] ?: "Unknown",
	syncGroupName = text["syncGroupName"] ?: "Unknown",
	syncGroupId = text["syncGroupId"] ?: "Unknown",
)
