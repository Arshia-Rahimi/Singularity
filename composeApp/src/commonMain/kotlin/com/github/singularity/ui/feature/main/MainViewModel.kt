package com.github.singularity.ui.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.singularity.core.shared.util.stateInWhileSubscribed
import com.github.singularity.core.sync.SyncService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModel(
    private val syncService: SyncService,
) : ViewModel() {

    private val connectionState = syncService.connectionState

    val uiState = combine(
        connectionState,
        syncService.syncMode,
    ) { connectionState, syncMode ->
        MainUiState(
            connectionState = connectionState,
            syncMode = syncMode,
        )
    }.stateInWhileSubscribed(MainUiState())

    fun execute(intent: MainIntent) {
        when (intent) {
            is MainIntent.RefreshConnection -> refreshConnection()
            is MainIntent.ToggleSyncMode -> toggleSyncMode()
            is MainIntent.ToDiscoverScreen -> Unit
            is MainIntent.ToSettingsScreen -> Unit
            is MainIntent.ToBroadcastScreen -> Unit
        }
    }

    private fun toggleSyncMode() {
        viewModelScope.launch { syncService.toggleSyncMode() }
    }

    private fun refreshConnection() {
        syncService.refreshClient()
    }

}
