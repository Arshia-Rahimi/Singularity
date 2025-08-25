package com.github.singularity.ui.feature.discover

import com.github.singularity.core.mdns.Server

sealed interface DiscoverIntent {
    data class SendPairRequest(val server: Server) : DiscoverIntent
    data object NavBack : DiscoverIntent
}
