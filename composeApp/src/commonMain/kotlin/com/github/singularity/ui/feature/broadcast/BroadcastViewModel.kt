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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class BroadcastViewModel(
    private val broadcastRepo: BroadcastRepository,
) : ViewModel() {

    private val syncGroups = broadcastRepo.syncGroups
        .map { it.sortedWith(compareBy<HostedSyncGroup> { group -> !group.isDefault }.thenBy { group -> group.name }) }
        .stateInWhileSubscribed(emptyList())

    private val isBroadcasting = broadcastRepo.isBroadcasting

    @OptIn(FlowPreview::class)
    val uiState = combine(
        syncGroups,
        isBroadcasting,
    ) { syncGroups, isBroadcasting ->
        BroadcastUiState(
            syncGroups = syncGroups.toMutableStateList(),
            isBroadcasting = isBroadcasting,
        )
    }.stateInWhileSubscribed(BroadcastUiState())

    fun execute(intent: BroadcastIntent) {
        when (intent) {
            is BroadcastIntent.Broadcast -> broadcast()
            is BroadcastIntent.StopBroadcast -> stopBroadcast()
            is BroadcastIntent.Approve -> approve(intent.node)
            is BroadcastIntent.CreateGroup -> create(intent.groupName)
            is BroadcastIntent.EditGroupName -> editName(intent.groupName, intent.group)
            is BroadcastIntent.DeleteGroup -> delete(intent.group)
            is BroadcastIntent.SetAsDefault -> setAsDefault(intent.group)
            is BroadcastIntent.NavBack -> Unit
        }
    }

    private fun broadcast() {
        viewModelScope.launch {
            broadcastRepo.stopBroadcast()
            broadcastRepo.startBroadcast()
        }
    }

    private fun setAsDefault(group: HostedSyncGroup) {
        viewModelScope.launch {
            broadcastRepo.setAsDefault(group)
        }
    }

    private fun stopBroadcast() {
        broadcastRepo.stopBroadcast()
    }

    private fun approve(node: Node) {
        broadcastRepo.approvePairRequest(node)
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
        broadcastRepo.stopBroadcast()
        super.onCleared()
    }

}
