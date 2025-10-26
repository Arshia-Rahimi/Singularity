package com.github.singularity.ui.feature.home.client

import androidx.lifecycle.ViewModel
import com.github.singularity.core.shared.model.ClientConnectionState
import com.github.singularity.core.shared.util.stateInWhileSubscribed
import com.github.singularity.core.sync.SyncService
import kotlinx.coroutines.flow.filterIsInstance

class ClientViewModel(
    private val syncService: SyncService,
) : ViewModel() {

    val connectionState = syncService.connectionState.filterIsInstance<ClientConnectionState>()
        .stateInWhileSubscribed(ClientConnectionState.NoDefaultServer)

    fun refreshConnection() {
        syncService.refresh()
    }

    fun toggleSyncMode() {
        syncService.toggleSyncMode()
    }

}