package com.github.singularity.ui.feature.home.broadcast

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.core.shared.model.Node
import com.github.singularity.core.shared.model.ServerConnectionState


@Immutable
data class BroadcastUiState(
    val connectionState: ServerConnectionState = ServerConnectionState.NoDefaultServer,
    val receivedPairRequests: SnapshotStateList<Node> = mutableStateListOf(),
    val hostedSyncGroups: SnapshotStateList<HostedSyncGroup> = mutableStateListOf(),
)
