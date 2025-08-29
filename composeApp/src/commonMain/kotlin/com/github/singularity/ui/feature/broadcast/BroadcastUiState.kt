package com.github.singularity.ui.feature.broadcast

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.github.singularity.core.database.entities.HostedSyncGroup
import com.github.singularity.models.Node

data class BroadcastUiState(
    val syncGroups: SnapshotStateList<HostedSyncGroup> = mutableStateListOf(),
    val defaultSyncGroup: HostedSyncGroup? = null,
    val requestedNodes: List<Node> = emptyList(),
)
