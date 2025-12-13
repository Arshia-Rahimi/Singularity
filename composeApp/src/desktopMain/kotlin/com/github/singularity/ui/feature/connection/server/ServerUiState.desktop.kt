package com.github.singularity.ui.feature.connection.server

import androidx.compose.runtime.Immutable
import com.github.singularity.core.datasource.database.HostedSyncGroupModel
import com.github.singularity.core.datasource.database.HostedSyncGroupNodeModel
import com.github.singularity.core.datasource.network.NodeDto
import com.github.singularity.core.syncservice.ServerSyncState

data class HostedSyncGroup(
	val groupName: String,
	val groupId: String,
	val pairedNodesCount: Int,
	val isDefault: Boolean,
)

fun HostedSyncGroupModel.toHostedSyncGroup() = HostedSyncGroup(
	groupId = hostedSyncGroupId,
	groupName = name,
	pairedNodesCount = nodes.size,
	isDefault = isDefault,
)

data class Node(
	val deviceId: String,
	val deviceName: String,
	val deviceOs: String,
)

data class RunningSyncGroupServer(
	val group: HostedSyncGroup,
	val connectedNodes: List<Node>,
	val requestedNodes: List<Node>,
	val pairedNodes: List<Node>,
)

fun HostedSyncGroupNodeModel.toNode() = Node(
	deviceName = deviceName,
	deviceId = deviceId,
	deviceOs = deviceOs,
)

fun NodeDto.toNode() = Node(
	deviceName = deviceName,
	deviceId = deviceId,
	deviceOs = deviceOs,
)

fun ServerSyncState.Running.toRunningSyncGroupServer() = RunningSyncGroupServer(
	group = group.toHostedSyncGroup(),
	pairedNodes = group.nodes.map { it.toNode() },
	connectedNodes = connectedNodes.map { it.toNode() },
	requestedNodes = pairRequests.map { it.toNode() },
)

@Immutable
data class ServerUiState(
	val connectionState: ServerSyncState = ServerSyncState.Loading,
    val hostedSyncGroups: List<HostedSyncGroup> = emptyList(),
)
