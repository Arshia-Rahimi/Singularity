package com.github.singularity.ui.feature.connection.client

sealed interface ClientIntent {
	data class SendPairRequest(val server: DiscoveredServer) : ClientIntent
	data class DeleteGroup(val group: PairedSyncGroup) : ClientIntent
	data class SetAsDefault(val group: PairedSyncGroup) : ClientIntent
    data object CancelPairRequest : ClientIntent
	data object RefreshConnection : ClientIntent
    data object ToIndex : ClientIntent
}
