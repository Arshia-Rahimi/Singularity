package com.github.singularity.ui.feature.connection.server

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.core.shared.model.ServerConnectionState

@Immutable
data class ServerUiState(
    val connectionState: ServerConnectionState = ServerConnectionState.NoDefaultServer,
    val hostedSyncGroups: SnapshotStateList<HostedSyncGroup> = mutableStateListOf(),
)
