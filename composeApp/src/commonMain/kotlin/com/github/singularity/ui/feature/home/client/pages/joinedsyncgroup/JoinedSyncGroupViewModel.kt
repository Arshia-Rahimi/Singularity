package com.github.singularity.ui.feature.home.client.pages.joinedsyncgroup

import androidx.lifecycle.ViewModel
import com.github.singularity.core.data.JoinedSyncGroupRepository
import com.github.singularity.core.shared.model.ClientConnectionState
import com.github.singularity.core.shared.util.stateInWhileSubscribed
import com.github.singularity.core.sync.SyncService
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance

class JoinedSyncGroupViewModel(
    joinedSyncGroupRepo: JoinedSyncGroupRepository,
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
        JoinedSyncGroupUiState(
            connectionState = connectionState,
            currentGroup = defaultGroup,
        )
    }.stateInWhileSubscribed(JoinedSyncGroupUiState())

    fun execute(intent: JoinedSyncGroupIntent) {
        when (intent) {
            is JoinedSyncGroupIntent.RefreshConnection -> refreshConnection()
        }
    }

    private fun refreshConnection() {
        syncService.refresh()
    }

}
