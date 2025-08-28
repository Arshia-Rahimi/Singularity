package com.github.singularity.ui.feature.discover

import com.github.singularity.core.shared.model.LocalServer

data class DiscoverUiState(
    val servers: List<LocalServer> = emptyList(),
)
