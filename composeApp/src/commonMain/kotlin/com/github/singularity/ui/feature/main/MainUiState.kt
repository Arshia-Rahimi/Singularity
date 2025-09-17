package com.github.singularity.ui.feature.main

import androidx.compose.runtime.Immutable
import com.github.singularity.core.shared.model.ConnectionState

@Immutable
data class MainUiState(
    val connectionState: ConnectionState = ConnectionState.NoDefaultServer,
)
