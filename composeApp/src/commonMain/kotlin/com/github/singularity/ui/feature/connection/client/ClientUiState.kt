package com.github.singularity.ui.feature.connection.client

import androidx.compose.runtime.Immutable
import com.github.singularity.core.datasource.database.JoinedSyncGroupModel
import com.github.singularity.core.datasource.network.LocalServerDto
import com.github.singularity.core.syncservice.ClientSyncState
import com.github.singularity.ui.feature.connection.client.pages.index.PairRequestState

data class DiscoveredServer(
	val groupName: String,
	val groupId: String,
	val deviceName: String,
	val deviceOs: String,
	val deviceId: String,
	val ip: String,
) {
	fun toLocalServer() = LocalServerDto(
		deviceName = deviceName,
		deviceOs = deviceOs,
		deviceId = deviceId,
		syncGroupId = groupId,
		syncGroupName = groupName,
		ip = ip,
	)
}

fun LocalServerDto.toDiscoveredServer() = DiscoveredServer(
	groupName = syncGroupName,
	groupId = syncGroupId,
	deviceName = deviceName,
	deviceOs = deviceOs,
	deviceId = deviceId,
	ip = ip,
)

data class PairedSyncGroup(
	val groupId: String,
	val groupName: String,
	val isAvailable: Boolean = false,
)

fun JoinedSyncGroupModel.toPairedSyncGroup() = PairedSyncGroup(
	groupId = syncGroupId,
	groupName = syncGroupName,
)

@Immutable
data class ClientUiState(
	val connectionState: ClientSyncState = ClientSyncState.Loading,
    val discoveredServers: List<DiscoveredServer> = emptyList(),
	val sentPairRequestState: PairRequestState = PairRequestState.Idle,
    val joinedSyncGroups: List<PairedSyncGroup> = emptyList(),
	val defaultSyncGroup: PairedSyncGroup? = null,
)
