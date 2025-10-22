package com.github.singularity.ui.feature.settings

import androidx.compose.runtime.Immutable
import com.github.singularity.core.shared.AppTheme

@Immutable
data class SettingsUiState(
    val theme: AppTheme = AppTheme.System,
    val scale: Float = 1f,
)
