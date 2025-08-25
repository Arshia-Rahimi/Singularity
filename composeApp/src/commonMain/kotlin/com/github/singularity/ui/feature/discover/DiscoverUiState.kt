package com.github.singularity.ui.feature.discover

import com.github.singularity.core.mdns.Server

data class DiscoverUiState(
    val servers: List<Server> = emptyList(),
)
