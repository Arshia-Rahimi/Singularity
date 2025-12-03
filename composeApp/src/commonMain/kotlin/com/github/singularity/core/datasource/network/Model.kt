package com.github.singularity.core.datasource.network

import com.github.singularity.core.datasource.database.HostedSyncGroupNodeModel
import kotlinx.serialization.Serializable

interface Device {
	val deviceName: String
	val deviceId: String
	val deviceOs: String
}

@Serializable
class NodeModel(
	override val deviceName: String,
	override val deviceId: String,
	override val deviceOs: String,
) : Device

@Serializable
data class LocalServerModel(
	override val deviceName: String,
	override val deviceId: String,
	override val deviceOs: String,
	val ip: String,
	val syncGroupName: String,
	val syncGroupId: String,
) : Device

@Serializable
data class PairRequestDto(
	val deviceName: String,
	val deviceId: String,
	val deviceOs: String,
	val syncGroupId: String,
	val syncGroupName: String,
) {
	fun toNode() = NodeModel(
		deviceName = deviceName,
		deviceId = deviceId,
		deviceOs = deviceOs,
	)
}

@Serializable
data class PairCheckRequestDto(
	val pairRequestId: Int,
	val syncGroupId: String,
)

@Serializable
data class PairResponseDto(
	val success: Boolean,
	val pairRequestId: Int? = null,
)

@Serializable
data class PairCheckResponseDto(
	val pairStatus: PairStatus,
	val message: String? = null,
	val node: HostedSyncGroupNodeModel? = null,
)

enum class PairStatus {
	Rejected, Awaiting, Approved, Error,
}
