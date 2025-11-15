package com.github.singularity.ui.feature.settings

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.github.singularity.core.shared.ThemeOption
import com.github.singularity.ui.feature.settings.components.DefaultPrimaryColor

@Immutable
data class SettingsUiState(
    val themeOption: ThemeOption = ThemeOption.System,
    val primaryColor: Color = DefaultPrimaryColor,
    val scale: Float = 1f,
)
