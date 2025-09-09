package com.github.singularity.ui.feature.discover

import com.github.singularity.core.shared.model.LocalServer

sealed interface DiscoverIntent {
    data class SendPairRequest(val server: LocalServer) : DiscoverIntent
    data object CancelPairRequest : DiscoverIntent
    data object NavBack : DiscoverIntent
}
