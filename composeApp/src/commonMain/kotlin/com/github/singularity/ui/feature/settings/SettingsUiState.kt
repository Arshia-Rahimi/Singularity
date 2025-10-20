package com.github.singularity.ui.feature.settings

import androidx.compose.runtime.Immutable
import com.github.singularity.ui.designsystem.theme.AppTheme

@Immutable
data class SettingsUiState(
    val theme: AppTheme = AppTheme.System,
    val scale: Float = 1f,
)
