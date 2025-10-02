package com.github.singularity.ui.navigation

import androidx.lifecycle.ViewModel
import com.github.singularity.core.data.PreferencesRepository
import com.github.singularity.core.shared.AppTheme
import com.github.singularity.core.shared.util.stateInWhileSubscribed
import com.github.singularity.core.sync.SyncService
import kotlinx.coroutines.flow.map

class NavigationViewModel(
    settingsRepo: PreferencesRepository,
    syncService: SyncService,
) : ViewModel() {

    val appTheme = settingsRepo.preferences.map { it.theme }
        .stateInWhileSubscribed(AppTheme.System)

    val syncMode = syncService.syncMode

}
