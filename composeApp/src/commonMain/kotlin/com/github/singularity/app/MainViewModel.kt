package com.github.singularity.app

import androidx.lifecycle.ViewModel
import com.github.singularity.core.data.PreferencesRepository
import com.github.singularity.core.shared.util.stateInWhileSubscribed
import com.github.singularity.core.sync.SyncService
import kotlinx.coroutines.flow.combine

class MainViewModel(
    preferencesRepo: PreferencesRepository,
    private val syncService: SyncService,
) : ViewModel() {

	val uiState = combine(preferencesRepo.preferences, syncService.syncMode) { prefs, syncMode ->
        MainUiState(
	        theme = prefs.theme,
	        scale = prefs.scale,
	        syncMode = syncMode,
        )
    }.stateInWhileSubscribed(null)

	fun toggleSyncMode() {
		syncService.toggleSyncMode()
	}

}
