package com.github.singularity.ui.feature.home.broadcast

import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.core.shared.model.Node


sealed interface BroadcastIntent {
    data class Approve(val node: Node) : BroadcastIntent
    data class Reject(val node: Node) : BroadcastIntent
    data class CreateGroup(val groupName: String) : BroadcastIntent
    data class DeleteGroup(val group: HostedSyncGroup) : BroadcastIntent
    data class SetAsDefault(val group: HostedSyncGroup) : BroadcastIntent
    data class EditGroupName(
        val groupName: String,
        val group: HostedSyncGroup,
    ) : BroadcastIntent

    data object RefreshConnection : BroadcastIntent
    data object ToggleSyncMode : BroadcastIntent
    data object OpenDrawer : BroadcastIntent
}
