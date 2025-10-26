package com.github.singularity.ui.feature.home.client.pages.joinedsyncgroup

import com.github.singularity.core.shared.model.ClientConnectionState
import com.github.singularity.core.shared.model.ConnectionState
import com.github.singularity.core.shared.model.JoinedSyncGroup

data class JoinedSyncGroupUiState(
    val connectionState: ConnectionState = ClientConnectionState.NoDefaultServer,
    val currentGroup: JoinedSyncGroup? = null,
)
