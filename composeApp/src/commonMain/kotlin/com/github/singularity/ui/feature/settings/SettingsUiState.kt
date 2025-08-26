package com.github.singularity.ui.feature.settings

import com.github.singularity.core.shared.AppTheme

data class SettingsUiState(
    val appTheme: AppTheme = AppTheme.System,
)
