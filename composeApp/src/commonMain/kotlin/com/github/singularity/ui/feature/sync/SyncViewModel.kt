package com.github.singularity.ui.feature.sync

import androidx.lifecycle.ViewModel
import com.github.singularity.core.shared.model.ClientConnectionState
import com.github.singularity.core.shared.util.stateInWhileSubscribed
import com.github.singularity.core.sync.SyncService
import kotlinx.coroutines.flow.combine

class SyncViewModel(
    private val syncService: SyncService,
) : ViewModel() {

    private val connectionState = syncService.connectionState
        .stateInWhileSubscribed(ClientConnectionState.NoDefaultServer)

    val uiState = combine(
        connectionState,
    ) {
        SyncUiState(
            connectionState = it[0],
        )
    }.stateInWhileSubscribed(SyncUiState())

    fun execute(intent: SyncIntent) {
        when (intent) {
            is SyncIntent.RefreshConnection -> syncService.refresh()
        }
    }

}