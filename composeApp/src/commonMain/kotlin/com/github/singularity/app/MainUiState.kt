package com.github.singularity.app

import androidx.compose.runtime.Immutable
import com.github.singularity.core.shared.SyncMode
import com.github.singularity.ui.designsystem.theme.AppTheme

@Immutable
data class MainUiState(
    val theme: AppTheme = AppTheme.System,
    val syncMode: SyncMode = SyncMode.Client,
    val scale: Float = 1f,
)
