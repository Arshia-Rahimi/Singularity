package com.github.singularity.ui.feature.discover

import androidx.compose.runtime.Immutable
import com.github.singularity.core.shared.model.LocalServer
import com.github.singularity.ui.feature.discover.components.PairRequestState

@Immutable
data class DiscoverUiState(
    val servers: List<LocalServer> = emptyList(),
    val pairRequestState: PairRequestState = PairRequestState.Idle,
)
