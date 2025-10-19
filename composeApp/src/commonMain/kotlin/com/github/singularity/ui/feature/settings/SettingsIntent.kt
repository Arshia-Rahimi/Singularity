package com.github.singularity.ui.feature.settings


sealed interface SettingsIntent {
    data object ToggleTheme: SettingsIntent
    data object OpenDrawer : SettingsIntent
    data class ChangeScale(val scale: Float) : SettingsIntent
}
