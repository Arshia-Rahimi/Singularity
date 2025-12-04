package com.github.singularity.core.datasource.network.impl

import com.bfo.zeroconf.Service
import com.github.singularity.core.datasource.network.LocalServerDto
import java.net.Inet4Address

sealed interface MdnsEvent {
	val server: LocalServerDto

	data class Resolved(override val server: LocalServerDto) : MdnsEvent
	data class Removed(override val server: LocalServerDto) : MdnsEvent
}


fun Service.toServer() = LocalServerDto(
	ip = addresses.firstOrNull {
		it is Inet4Address && !it.isLoopbackAddress
	}?.hostAddress ?: "Unknown",
	deviceOs = text["deviceOs"] ?: "Unknown",
	deviceName = text["deviceName"] ?: "Unknown",
	deviceId = text["deviceId"] ?: "Unknown",
	syncGroupName = text["syncGroupName"] ?: "Unknown",
	syncGroupId = text["syncGroupId"] ?: "Unknown",
)
