package com.github.singularity.ui.feature.main

import com.github.singularity.core.shared.model.ConnectionState

data class MainUiState(
    val connectionState: ConnectionState = ConnectionState.NoDefaultServer,
)
