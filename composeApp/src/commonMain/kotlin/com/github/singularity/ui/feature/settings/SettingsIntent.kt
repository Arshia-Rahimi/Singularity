package com.github.singularity.ui.feature.settings

sealed interface SettingsIntent {
    data object ToggleAppTheme: SettingsIntent
    data object ToLogScreen: SettingsIntent
    data object NavBack: SettingsIntent
}