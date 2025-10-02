package com.github.singularity.ui.feature.discover

import com.github.singularity.core.shared.model.LocalServer


sealed interface DiscoverIntent {
    data class SendPairRequest(val server: LocalServer) : DiscoverIntent
    data object CancelPairRequest : DiscoverIntent
    data object RefreshDiscovery : DiscoverIntent
    data object RefreshConnection : DiscoverIntent
    data object ToSettingsScreen : DiscoverIntent
    data object ToggleSyncMode : DiscoverIntent
}
