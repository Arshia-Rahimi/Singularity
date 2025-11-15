package com.github.singularity.ui.feature.settings

import androidx.compose.runtime.Immutable
import com.github.singularity.core.shared.ThemeOption

@Immutable
data class SettingsUiState(
    val themeOption: ThemeOption = ThemeOption.System,
    val scale: Float = 1f,
)
