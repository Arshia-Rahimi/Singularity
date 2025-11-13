package com.github.singularity.app

import androidx.compose.runtime.Immutable
import com.github.singularity.core.shared.AppTheme
import com.github.singularity.core.shared.SyncMode

@Immutable
data class MainUiState(
    val theme: AppTheme = AppTheme.System,
    val scale: Float = 1f,
    val syncMode: SyncMode = SyncMode.Client,
)
