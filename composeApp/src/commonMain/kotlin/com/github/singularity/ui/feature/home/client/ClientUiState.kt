package com.github.singularity.ui.feature.home.client

import com.github.singularity.core.shared.model.JoinedSyncGroup

data class ClientUiState(
    val defaultGroup: JoinedSyncGroup? = null,
)
