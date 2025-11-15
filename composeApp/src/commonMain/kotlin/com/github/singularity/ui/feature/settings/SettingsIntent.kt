package com.github.singularity.ui.feature.settings

import androidx.compose.ui.graphics.Color


sealed interface SettingsIntent {
    data object ToggleThemeOption : SettingsIntent
    data class ChangePrimaryColor(val color: Color) : SettingsIntent
    data class ChangeScale(val scale: Float) : SettingsIntent
}
