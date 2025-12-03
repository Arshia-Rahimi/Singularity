package com.github.singularity.ui.feature.connection.server

import com.github.singularity.core.datasource.network.NodeModel

sealed interface ServerIntent {
	data class Approve(val node: NodeModel) : ServerIntent
	data class Reject(val node: NodeModel) : ServerIntent
    data class CreateGroup(val groupName: String) : ServerIntent
    data class DeleteGroup(val group: HostedSyncGroup) : ServerIntent
    data class SetAsDefault(val group: HostedSyncGroup) : ServerIntent
    data class EditGroupName(
        val groupName: String,
        val group: HostedSyncGroup,
    ) : ServerIntent

    data object RefreshConnection : ServerIntent
    data object ToIndex : ServerIntent
}
