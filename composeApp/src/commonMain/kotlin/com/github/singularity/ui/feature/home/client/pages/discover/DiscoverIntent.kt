package com.github.singularity.ui.feature.home.client.pages.discover

import com.github.singularity.core.shared.model.JoinedSyncGroup
import com.github.singularity.core.shared.model.LocalServer


sealed interface DiscoverIntent {
    data class SendPairRequest(val server: LocalServer) : DiscoverIntent
    data class SetAsDefault(val group: JoinedSyncGroup) : DiscoverIntent
    data class DeleteGroup(val group: JoinedSyncGroup) : DiscoverIntent
    data object RemoveAllDefaults : DiscoverIntent
    data object CancelPairRequest : DiscoverIntent
    data object RefreshDiscovery : DiscoverIntent
    data object OpenDrawer : DiscoverIntent
    data object StartDiscovery : DiscoverIntent
}
