package com.github.singularity.ui.feature.home.client

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.singularity.app.navigation.components.AppNavigationController
import com.github.singularity.core.data.JoinedSyncGroupRepository
import com.github.singularity.core.shared.model.JoinedSyncGroup
import com.github.singularity.core.shared.util.stateInWhileSubscribed
import com.github.singularity.core.sync.SyncService
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class ClientViewModel(
    private val syncService: SyncService,
    private val joinedSyncGroupRepo: JoinedSyncGroupRepository,
) : ViewModel() {

    private val defaultGroup = joinedSyncGroupRepo.defaultJoinedSyncGroup
        .stateInWhileSubscribed(null)

    val uiState = combine(
        defaultGroup,
    ) {
        ClientUiState(
            defaultGroup = it[0],
        )
    }.stateInWhileSubscribed(ClientUiState())

    fun execute(intent: ClientIntent) {
        when (intent) {
            is ClientIntent.ToggleSyncMode -> toggleSyncMode()
            is ClientIntent.SetAsDefault -> setAsDefault(intent.group)
            is ClientIntent.OpenDrawer -> AppNavigationController.toggleDrawer()
        }
    }

    private fun toggleSyncMode() {
        viewModelScope.launch {
            syncService.toggleSyncMode()
        }
    }

    private fun setAsDefault(group: JoinedSyncGroup) {
        viewModelScope.launch {
            joinedSyncGroupRepo.setAsDefault(group)
        }
    }

}
