package com.github.singularity.ui.feature.connection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.singularity.core.shared.SyncMode
import com.github.singularity.core.shared.util.stateInWhileSubscribed
import com.github.singularity.core.sync.SyncService
import kotlinx.coroutines.launch

class ConnectionViewModel(
    private val syncService: SyncService,
) : ViewModel() {

    val syncMode = syncService.syncMode.stateInWhileSubscribed(SyncMode.Client)

    fun toggleSyncMode() {
        viewModelScope.launch {
            syncService.toggleSyncMode()
        }
    }

}
