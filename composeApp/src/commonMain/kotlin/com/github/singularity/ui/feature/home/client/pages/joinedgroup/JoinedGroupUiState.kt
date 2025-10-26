package com.github.singularity.ui.feature.home.client.pages.joinedgroup

import com.github.singularity.core.shared.model.ClientConnectionState
import com.github.singularity.core.shared.model.ConnectionState
import com.github.singularity.core.shared.model.JoinedSyncGroup

data class JoinedGroupUiState(
    val connectionState: ConnectionState = ClientConnectionState.NoDefaultServer,
    val currentGroup: JoinedSyncGroup? = null,
)
