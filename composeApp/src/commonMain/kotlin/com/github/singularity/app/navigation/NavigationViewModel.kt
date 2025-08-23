package com.github.singularity.app.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.singularity.core.data.PreferencesRepository
import com.github.singularity.core.shared.AppTheme
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class NavigationViewModel(
    settingsRepo: PreferencesRepository,
) : ViewModel() {
    
    val appTheme = settingsRepo.preferences
        .map { it.theme }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppTheme.System)

}
