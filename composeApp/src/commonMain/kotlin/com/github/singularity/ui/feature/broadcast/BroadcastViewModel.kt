package com.github.singularity.ui.feature.broadcast

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.singularity.core.data.BroadcastRepository
import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.core.shared.model.Node
import com.github.singularity.core.shared.util.stateInWhileSubscribed
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch

class BroadcastViewModel(
    private val broadcastRepo: BroadcastRepository,
) : ViewModel() {

    private val syncGroups = broadcastRepo.syncGroups
        .stateInWhileSubscribed(emptyList())

    private val pairRequests = broadcastRepo.pairRequests

    private val isBroadcasting = broadcastRepo.isBroadcasting

    @OptIn(FlowPreview::class)
    val uiState = combine(
        syncGroups,
        isBroadcasting,
        pairRequests,
    ) { syncGroups, isBroadcasting, pairRequests ->
        BroadcastUiState(
            syncGroups = syncGroups.toMutableStateList(),
            isBroadcasting = isBroadcasting,
            pairRequests = pairRequests,
        )
    }.stateInWhileSubscribed(BroadcastUiState())

    fun execute(intent: BroadcastIntent) {
        when (intent) {
            is BroadcastIntent.Broadcast -> broadcast()
            is BroadcastIntent.StopBroadcast -> stopBroadcast()
            is BroadcastIntent.Approve -> approve(intent.node)
            is BroadcastIntent.Reject -> reject(intent.node)
            is BroadcastIntent.CreateGroup -> create(intent.groupName)
            is BroadcastIntent.EditGroupName -> editName(intent.groupName, intent.group)
            is BroadcastIntent.DeleteGroup -> delete(intent.group)
            is BroadcastIntent.SetAsDefault -> setAsDefault(intent.group)
            is BroadcastIntent.NavBack -> Unit
        }
    }

    private fun broadcast() {
        viewModelScope.launch {
            if (isBroadcasting.value) broadcastRepo.stopBroadcast()
            broadcastRepo.startBroadcast()
        }
    }

    private fun stopBroadcast() {
        viewModelScope.launch {
            broadcastRepo.stopBroadcast()
        }
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
