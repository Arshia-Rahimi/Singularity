package com.github.singularity.ui.feature.main

import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.core.shared.model.LocalServer
import com.github.singularity.core.shared.model.Node


sealed interface MainIntent {

    data object ToSettingsScreen : MainIntent
    data object RefreshConnection : MainIntent
    data object ToggleSyncMode : MainIntent

    sealed interface BroadcastIntent : MainIntent {
        data class Approve(val node: Node) : BroadcastIntent
        data class Reject(val node: Node) : BroadcastIntent
        data class CreateGroup(val groupName: String) : BroadcastIntent
        data class DeleteGroup(val group: HostedSyncGroup) : BroadcastIntent
        data class SetAsDefault(val group: HostedSyncGroup) : BroadcastIntent
        data class EditGroupName(
            val groupName: String,
            val group: HostedSyncGroup,
        ) : BroadcastIntent
    }

    sealed interface DiscoverIntent : MainIntent {
        data class SendPairRequest(val server: LocalServer) : DiscoverIntent
        data object CancelPairRequest : DiscoverIntent
        data object RefreshDiscovery : DiscoverIntent
    }

}
