package com.github.singularity.ui.feature.connection.server

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.github.singularity.core.datasource.database.HostedSyncGroupModel
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

@Immutable
data class ServerUiState(
	val connectionState: ServerSyncState = ServerSyncState.Loading,
	val hostedSyncGroups: SnapshotStateList<HostedSyncGroup> = mutableStateListOf(),
)
