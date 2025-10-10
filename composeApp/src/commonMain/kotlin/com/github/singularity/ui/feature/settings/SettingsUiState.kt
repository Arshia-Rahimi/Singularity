package com.github.singularity.ui.feature.settings

import androidx.compose.runtime.Immutable
import com.github.singularity.core.shared.AppTheme
import com.github.singularity.core.shared.ScaleOption

@Immutable
data class SettingsUiState(
    val theme: AppTheme = AppTheme.System,
    val scale: ScaleOption = ScaleOption.S100,
)
