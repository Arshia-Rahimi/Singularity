package com.github.singularity.ui.feature.home.client

import com.github.singularity.core.shared.model.JoinedSyncGroup
import com.github.singularity.core.shared.model.LocalServer


sealed interface ClientIntent {
    data class SendPairRequest(val server: LocalServer) : ClientIntent
    data class SetAsDefault(val group: JoinedSyncGroup) : ClientIntent
    data class DeleteGroup(val group: JoinedSyncGroup) : ClientIntent
    data object CancelPairRequest : ClientIntent
    data object RefreshDiscovery : ClientIntent
    data object RefreshConnection : ClientIntent
    data object ToggleSyncMode : ClientIntent
    data object OpenDrawer : ClientIntent
}
