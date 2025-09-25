package com.github.singularity.ui.feature.main

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.github.singularity.core.shared.SyncMode
import com.github.singularity.core.shared.model.ClientConnectionState
import com.github.singularity.core.shared.model.ConnectionState
import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.core.shared.model.LocalServer
import com.github.singularity.core.shared.model.Node
import com.github.singularity.ui.feature.main.components.discover.PairRequestState

@Immutable
data class MainUiState(
    val syncMode: SyncMode = SyncMode.Client,
    val connectionState: ConnectionState = ClientConnectionState.NoDefaultServer,
    val discoverUiState: DiscoverUiState = DiscoverUiState(),
    val broadcastUiState: BroadcastUiState = BroadcastUiState(),
)

@Immutable
data class DiscoverUiState(
    val availableServers: SnapshotStateList<LocalServer> = mutableStateListOf(),
    val sentPairRequestState: PairRequestState = PairRequestState.Idle,
)

@Immutable
data class BroadcastUiState(
    val receivedPairRequests: SnapshotStateList<Node> = mutableStateListOf(),
    val hostedSyncGroups: SnapshotStateList<HostedSyncGroup> = mutableStateListOf(),
)
