package com.github.singularity.ui.feature.home.client.pages.joinedgroup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.singularity.core.data.JoinedSyncGroupRepository
import com.github.singularity.core.shared.model.ClientConnectionState
import com.github.singularity.core.shared.util.stateInWhileSubscribed
import com.github.singularity.core.sync.SyncService
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch

class JoinedGroupViewModel(
    private val joinedSyncGroupRepo: JoinedSyncGroupRepository,
    private val syncService: SyncService,
) : ViewModel() {

    private val connectionState =
        syncService.connectionState.filterIsInstance<ClientConnectionState>()
            .stateInWhileSubscribed(ClientConnectionState.NoDefaultServer)

    private val defaultGroup = joinedSyncGroupRepo.defaultJoinedSyncGroup
        .stateInWhileSubscribed(null)

    val uiState = combine(
        connectionState,
        defaultGroup,
    ) { connectionState, defaultGroup ->
        JoinedGroupUiState(
            connectionState = connectionState,
            currentGroup = defaultGroup,
        )
    }.stateInWhileSubscribed(JoinedGroupUiState())

    fun execute(intent: JoinedGroupIntent) {
        when (intent) {
            is JoinedGroupIntent.RefreshConnection -> refreshConnection()
            is JoinedGroupIntent.RemoveAllDefaults -> removeAllDefaults()
        }
    }

    private fun refreshConnection() {
        syncService.refresh()
    }

    private fun removeAllDefaults() {
        viewModelScope.launch {
            joinedSyncGroupRepo.removeAllDefaults()
        }
    }

}
