package com.github.singularity.ui.feature.broadcast

import com.github.singularity.core.database.entities.HostedSyncGroup
import com.github.singularity.models.Node

sealed interface BroadcastIntent {
    data class Broadcast(val group: HostedSyncGroup): BroadcastIntent
    data class Approve(val node: Node): BroadcastIntent
    data class CreateGroup(val groupName: String): BroadcastIntent
    data class DeleteGroup(val hostedSyncGroup: HostedSyncGroup): BroadcastIntent
    data object NavBack : BroadcastIntent
}

