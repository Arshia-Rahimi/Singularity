package com.github.singularity.ui.feature.broadcast

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.singularity.core.data.BroadcastRepository
import com.github.singularity.core.database.entities.HostedSyncGroup
import com.github.singularity.core.shared.util.Resource
import com.github.singularity.models.Node
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BroadcastViewModel(
    private val broadcastRepo: BroadcastRepository,
) : ViewModel() {

    private val syncGroups = broadcastRepo.syncGroups
        .map { it.sortedWith(compareBy<HostedSyncGroup> { group -> !group.isDefault }.thenBy { group -> group.name }) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val requestedNodes = MutableStateFlow(emptyList<Node>())

    val uiState = requestedNodes.combine(syncGroups) { requestedNodes, syncGroups ->
        BroadcastUiState(
            syncGroups = syncGroups.toMutableStateList(),
            defaultSyncGroup = syncGroups.firstOrNull { group -> group.isDefault },
            requestedNodes = requestedNodes,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BroadcastUiState())

    fun execute(intent: BroadcastIntent) {
        when (intent) {
            is BroadcastIntent.Broadcast -> broadcast()
            is BroadcastIntent.Approve -> approve(intent.node)
            is BroadcastIntent.CreateGroup -> create(intent.groupName)
            is BroadcastIntent.DeleteGroup -> delete(intent.group)
            is BroadcastIntent.SetAsDefault -> setAsDefault(intent.group)
            is BroadcastIntent.NavBack -> Unit
        }
    }

    private fun broadcast() {
        broadcastRepo.stopBroadcast()
        val defaultGroup = syncGroups.value.firstOrNull { it.isDefault } ?: return

        broadcastRepo.broadcastGroup(defaultGroup).onEach {
            requestedNodes.value = requestedNodes.value + it
        }.launchIn(viewModelScope)
    }

    private fun approve(node: Node) {
        broadcastRepo.approvePairRequest(node).onEach {
            if (it !is Resource.Loading) {
                requestedNodes.value = requestedNodes.value - node
            }
        }.launchIn(viewModelScope)
    }

    private fun create(groupName: String) {
        broadcastRepo.create(HostedSyncGroup(groupName)).launchIn(viewModelScope)
    }

    private fun delete(group: HostedSyncGroup) {
        broadcastRepo.delete(group).launchIn(viewModelScope)
    }

    private fun setAsDefault(group: HostedSyncGroup) {
        viewModelScope.launch { broadcastRepo.setAsDefault(group) }
    }

    override fun onCleared() {
        broadcastRepo.stopBroadcast()
        super.onCleared()
    }

}
