package com.github.singularity.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.singularity.core.data.SettingsRepository
import com.github.singularity.core.shared.AppTheme
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MainViewModel(
    settingsRepo: SettingsRepository,
) : ViewModel() {

    val appTheme = settingsRepo.settings
        .map { it.theme }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppTheme.System)

}
