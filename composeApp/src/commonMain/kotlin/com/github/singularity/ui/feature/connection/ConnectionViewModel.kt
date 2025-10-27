package com.github.singularity.ui.feature.connection

import androidx.lifecycle.ViewModel
import com.github.singularity.core.shared.SyncMode
import com.github.singularity.core.shared.util.stateInWhileSubscribed
import com.github.singularity.core.sync.SyncService

class ConnectionViewModel(
    private val syncService: SyncService,
) : ViewModel() {

    val syncMode = syncService.syncMode.stateInWhileSubscribed(SyncMode.Client)

    fun toggleSyncMode() {
        syncService.toggleSyncMode()
    }

}