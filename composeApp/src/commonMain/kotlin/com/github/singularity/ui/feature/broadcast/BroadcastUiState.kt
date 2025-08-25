package com.github.singularity.ui.feature.broadcast

import com.github.singularity.core.database.entities.HostedSyncGroup

data class BroadcastUiState(
    val syncGroups: List<HostedSyncGroup> = emptyList(),
    val defaultSyncGroup: HostedSyncGroup? = null,
)
