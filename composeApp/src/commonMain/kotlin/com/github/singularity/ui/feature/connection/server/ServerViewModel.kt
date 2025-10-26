package com.github.singularity.ui.feature.connection.server

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.singularity.app.navigation.components.AppNavigationController
import com.github.singularity.core.data.BroadcastRepository
import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.core.shared.model.Node
import com.github.singularity.core.shared.model.ServerConnectionState
import com.github.singularity.core.shared.util.stateInWhileSubscribed
import com.github.singularity.core.sync.SyncService
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch

class ServerViewModel(
    private val broadcastRepo: BroadcastRepository,
    private val syncService: SyncService,
) : ViewModel() {

    private val connectionState = syncService.connectionState
        .filterIsInstance<ServerConnectionState>()

    private val hostedSyncGroups = broadcastRepo.syncGroups
        .stateInWhileSubscribed(emptyList())

    private val receivedPairRequests = broadcastRepo.pairRequests
        .stateInWhileSubscribed(emptyList())

    val uiState = combine(
        connectionState,
        hostedSyncGroups,
        receivedPairRequests,
    ) { connectionState, hostedSyncGroups, receivedPairRequests ->
        ServerUiState(
            connectionState = connectionState,
            hostedSyncGroups = hostedSyncGroups.toMutableStateList(),
            receivedPairRequests = receivedPairRequests.toMutableStateList(),
        )
    }.stateInWhileSubscribed(ServerUiState())

    fun execute(intent: ServerIntent) {
        when (intent) {
            is ServerIntent.Approve -> approve(intent.node)
            is ServerIntent.Reject -> reject(intent.node)
            is ServerIntent.CreateGroup -> create(intent.groupName)
            is ServerIntent.EditGroupName -> editName(intent.groupName, intent.group)
            is ServerIntent.DeleteGroup -> delete(intent.group)
            is ServerIntent.SetAsDefault -> setAsDefault(intent.group)
            is ServerIntent.RefreshConnection -> refreshConnection()
            is ServerIntent.ToggleSyncMode -> syncService.toggleSyncMode()
            is ServerIntent.OpenDrawer -> AppNavigationController.toggleDrawer()
        }
    }

    private fun refreshConnection() {
        syncService.refresh()
    }

    private fun setAsDefault(group: HostedSyncGroup) {
        viewModelScope.launch {
            broadcastRepo.setAsDefault(group)
        }
    }

    private fun approve(node: Node) {
        broadcastRepo.approvePairRequest(node)
    }

    private fun reject(node: Node) {
        broadcastRepo.rejectPairRequest(node)
    }

    private fun create(groupName: String) {
        broadcastRepo.create(HostedSyncGroup(groupName)).launchIn(viewModelScope)
    }

    private fun editName(groupName: String, group: HostedSyncGroup) {
        broadcastRepo.editName(groupName, group).launchIn(viewModelScope)
    }

    private fun delete(group: HostedSyncGroup) {
        broadcastRepo.delete(group).launchIn(viewModelScope)
    }

    override fun onCleared() {
        viewModelScope.launch {
            broadcastRepo.stopBroadcast()
            super.onCleared()
        }
    }

}
