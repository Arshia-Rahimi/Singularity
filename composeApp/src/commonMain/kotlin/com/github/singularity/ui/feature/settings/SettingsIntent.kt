package com.github.singularity.ui.feature.settings

import com.github.singularity.core.shared.ScaleOption


sealed interface SettingsIntent {
    data object ToggleTheme: SettingsIntent
    data object OpenDrawer : SettingsIntent
    data class ChangeScale(val scale: ScaleOption): SettingsIntent
}
