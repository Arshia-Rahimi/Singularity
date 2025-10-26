package com.github.singularity.ui.feature.home.client.pages.discover

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.github.singularity.core.shared.model.JoinedSyncGroup
import com.github.singularity.core.shared.model.LocalServer
import com.github.singularity.ui.feature.home.client.pages.discover.components.PairRequestState

@Immutable
data class DiscoverUiState(
    val availableServers: SnapshotStateList<LocalServer>? = null,
    val sentPairRequestState: PairRequestState = PairRequestState.Idle,
    val joinedSyncGroups: SnapshotStateList<JoinedSyncGroup> = mutableStateListOf(),
    val defaultSyncGroup: JoinedSyncGroup? = null,
)
