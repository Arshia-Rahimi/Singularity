package com.github.singularity.ui.feature.home.discover

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.github.singularity.core.shared.model.ClientConnectionState
import com.github.singularity.core.shared.model.JoinedSyncGroup
import com.github.singularity.core.shared.model.LocalServer
import com.github.singularity.ui.feature.home.discover.components.PairRequestState

@Immutable
data class DiscoverUiState(
    val connectionState: ClientConnectionState = ClientConnectionState.NoDefaultServer,
    val availableServers: SnapshotStateList<LocalServer> = mutableStateListOf(),
    val sentPairRequestState: PairRequestState = PairRequestState.Idle,
    val joinedSyncGroups: SnapshotStateList<JoinedSyncGroup> = mutableStateListOf(),
)
