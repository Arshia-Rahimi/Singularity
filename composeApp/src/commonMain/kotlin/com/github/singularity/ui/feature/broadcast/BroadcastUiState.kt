package com.github.singularity.ui.feature.broadcast

import com.github.singularity.core.database.entities.HostedSyncGroup
import com.github.singularity.core.mdns.Node

data class BroadcastUiState(
    val syncGroups: List<HostedSyncGroup> = emptyList(),
    val defaultSyncGroup: HostedSyncGroup? = null,
    val requestedNodes: List<Node> = emptyList(),
)
