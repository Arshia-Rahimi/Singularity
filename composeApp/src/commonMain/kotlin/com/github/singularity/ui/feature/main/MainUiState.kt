package com.github.singularity.ui.feature.main

import androidx.compose.runtime.Immutable
import com.github.singularity.core.shared.SyncMode
import com.github.singularity.core.shared.model.ClientConnectionState
import com.github.singularity.core.shared.model.ConnectionState

@Immutable
data class MainUiState(
    val syncMode: SyncMode = SyncMode.Client,
    val connectionState: ConnectionState = ClientConnectionState.NoDefaultServer,
)
