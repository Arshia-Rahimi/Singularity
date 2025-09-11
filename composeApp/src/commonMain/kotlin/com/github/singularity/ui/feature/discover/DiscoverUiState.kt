package com.github.singularity.ui.feature.discover

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.github.singularity.core.shared.model.LocalServer
import com.github.singularity.ui.feature.discover.components.PairRequestState

@Immutable
data class DiscoverUiState(
    val servers: SnapshotStateList<LocalServer> = mutableStateListOf(),
    val pairRequestState: PairRequestState = PairRequestState.Idle,
)
