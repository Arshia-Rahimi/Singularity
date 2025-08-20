package com.github.singularity.ui.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.singularity.core.common.util.next
import com.github.singularity.core.data.SettingsRepository
import com.github.singularity.core.shared.AppTheme
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val settingsRepo: SettingsRepository,
) : ViewModel() {

    val appTheme = settingsRepo.settings
        .map { it.theme }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppTheme.System)

    fun toggleTheme() {
        viewModelScope.launch {
            settingsRepo.setAppTheme(appTheme.first().next())
        }
    }
}
