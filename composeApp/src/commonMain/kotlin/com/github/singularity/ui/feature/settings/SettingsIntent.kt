package com.github.singularity.ui.feature.settings

sealed interface SettingsIntent {
    data object ToggleApptheme: SettingsIntent
    data object NavBack: SettingsIntent
}