package com.github.singularity.ui.feature.sync

import com.github.singularity.core.shared.model.ClientConnectionState
import com.github.singularity.core.shared.model.ConnectionState

data class SyncUiState(
    val connectionState: ConnectionState = ClientConnectionState.NoDefaultServer,
)
