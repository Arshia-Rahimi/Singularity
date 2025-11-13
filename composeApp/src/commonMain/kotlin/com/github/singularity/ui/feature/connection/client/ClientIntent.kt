package com.github.singularity.ui.feature.connection.client

import com.github.singularity.core.shared.model.JoinedSyncGroup
import com.github.singularity.core.shared.model.LocalServer

sealed interface ClientIntent {
    data class SendPairRequest(val server: LocalServer) : ClientIntent
    data class DeleteGroup(val group: JoinedSyncGroup) : ClientIntent
    data class SetAsDefault(val group: JoinedSyncGroup) : ClientIntent
    data object CancelPairRequest : ClientIntent
    data object StartDiscovery : ClientIntent
    data object StopDiscovery : ClientIntent
}
