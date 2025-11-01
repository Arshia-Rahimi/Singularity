package com.github.singularity.ui.feature.sync

import androidx.compose.runtime.Immutable
import com.github.singularity.core.shared.model.ClientConnectionState
import com.github.singularity.core.shared.model.ConnectionState

@Immutable
data class SyncUiState(
    val connectionState: ConnectionState = ClientConnectionState.NoDefaultServer,
)
