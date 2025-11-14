package com.github.singularity.ui.feature.connection.client

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.github.singularity.core.shared.model.ClientConnectionState
import com.github.singularity.core.shared.model.JoinedSyncGroup
import com.github.singularity.core.shared.model.LocalServer
import com.github.singularity.ui.feature.connection.client.pages.index.PairRequestState

@Immutable
data class ClientUiState(
	val connectionState: ClientConnectionState? = null,
    val availableServers: SnapshotStateList<LocalServer> = mutableStateListOf(),
    val isDiscovering: Boolean = false,
    val sentPairRequestState: PairRequestState = PairRequestState.Idle,
    val joinedSyncGroups: SnapshotStateList<JoinedSyncGroup> = mutableStateListOf(),
    val defaultSyncGroup: JoinedSyncGroup? = null,
)
