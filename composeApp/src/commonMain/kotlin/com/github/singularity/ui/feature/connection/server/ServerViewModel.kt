package com.github.singularity.ui.feature.connection.server

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.singularity.core.data.BroadcastRepository
import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.core.shared.model.ServerConnectionState
import com.github.singularity.core.shared.util.stateInWhileSubscribed
import com.github.singularity.core.sync.SyncService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ServerViewModel(
    private val broadcastRepo: BroadcastRepository,
    private val syncService: SyncService,
) : ViewModel() {

    private val connectionState = syncService.connectionState
        .filterIsInstance<ServerConnectionState>()
	    .stateInWhileSubscribed(null)

    private val hostedSyncGroups = broadcastRepo.syncGroups
        .stateInWhileSubscribed(emptyList())

    val uiState = combine(
        connectionState,
        hostedSyncGroups,
    ) { connectionState, hostedSyncGroups ->
        ServerUiState(
            connectionState = connectionState,
	        hostedSyncGroups = hostedSyncGroups.distinctBy { it.hostedSyncGroupId }
		        .toMutableStateList(),
        )
    }.stateInWhileSubscribed(ServerUiState())

    fun execute(intent: ServerIntent) {
        when (intent) {
            is ServerIntent.Approve -> broadcastRepo.approvePairRequest(intent.node)
            is ServerIntent.Reject -> broadcastRepo.rejectPairRequest(intent.node)
            is ServerIntent.CreateGroup -> create(intent.groupName)
            is ServerIntent.EditGroupName -> editName(intent.groupName, intent.group)
            is ServerIntent.DeleteGroup -> delete(intent.group)
            is ServerIntent.SetAsDefault -> setAsDefault(intent.group)
            is ServerIntent.RefreshConnection -> syncService.refresh()
            is ServerIntent.RemoveAllDefaults -> removeAllDefaults()
        }
    }

    private fun setAsDefault(group: HostedSyncGroup) {
        viewModelScope.launch {
            broadcastRepo.setAsDefault(group)
        }
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

    private fun removeAllDefaults() {
        viewModelScope.launch {
            broadcastRepo.removeAllDefaults()
        }
    }

}
