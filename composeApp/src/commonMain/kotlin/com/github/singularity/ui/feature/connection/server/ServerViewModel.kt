package com.github.singularity.ui.feature.connection.server

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.singularity.app.navigation.components.AppNavigationController
import com.github.singularity.core.data.BroadcastRepository
import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.core.shared.model.ServerConnectionState
import com.github.singularity.core.shared.util.stateInWhileSubscribed
import com.github.singularity.core.sync.SyncService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ServerViewModel(
    private val broadcastRepo: BroadcastRepository,
    private val syncService: SyncService,
) : ViewModel() {

    private val connectionState = syncService.connectionState
        .filterIsInstance<ServerConnectionState>()

    private val hostedSyncGroups = broadcastRepo.syncGroups
        .stateInWhileSubscribed(emptyList())

    private val shouldBroadcast = MutableStateFlow(false)

    private val receivedPairRequests = shouldBroadcast.flatMapLatest { shouldBroadcast ->
        if (shouldBroadcast) broadcastRepo.broadcast()
        else emptyFlow()
    }.stateInWhileSubscribed(emptyList())

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
            is ServerIntent.Approve -> broadcastRepo.approvePairRequest(intent.node)
            is ServerIntent.Reject -> broadcastRepo.rejectPairRequest(intent.node)
            is ServerIntent.CreateGroup -> create(intent.groupName)
            is ServerIntent.EditGroupName -> editName(intent.groupName, intent.group)
            is ServerIntent.DeleteGroup -> delete(intent.group)
            is ServerIntent.SetAsDefault -> setAsDefault(intent.group)
            is ServerIntent.RefreshConnection -> syncService.refresh()
            is ServerIntent.ToggleSyncMode -> syncService.toggleSyncMode()
            is ServerIntent.OpenDrawer -> AppNavigationController.toggleDrawer()
            is ServerIntent.StartBroadcast -> shouldBroadcast.value = true
            is ServerIntent.StopBroadcast -> shouldBroadcast.value = false
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

}
