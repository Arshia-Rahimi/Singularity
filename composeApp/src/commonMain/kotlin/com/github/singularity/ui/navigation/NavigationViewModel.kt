package com.github.singularity.ui.navigation

import androidx.lifecycle.ViewModel
import com.github.singularity.core.data.PreferencesRepository
import com.github.singularity.core.shared.SyncMode
import com.github.singularity.core.shared.util.stateInWhileSubscribed
import kotlinx.coroutines.flow.map

class NavigationViewModel(
    settingsRepo: PreferencesRepository,
    preferencesRepo: PreferencesRepository,
) : ViewModel() {

    val appTheme = settingsRepo.preferences.map { it.theme }
        .stateInWhileSubscribed(null)

    val syncMode = preferencesRepo.preferences.map { it.syncMode }
        .stateInWhileSubscribed(SyncMode.Client)
    
}
