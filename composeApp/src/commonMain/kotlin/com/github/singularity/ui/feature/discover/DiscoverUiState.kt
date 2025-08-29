package com.github.singularity.ui.feature.discover

import androidx.compose.runtime.Immutable
import com.github.singularity.core.shared.model.LocalServer

@Immutable
data class DiscoverUiState(
    val servers: List<LocalServer> = emptyList(),
)
