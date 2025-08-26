package com.github.singularity.ui.feature.broadcast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.singularity.core.common.util.Resource
import com.github.singularity.core.data.BroadcastRepository
import com.github.singularity.core.database.entities.HostedSyncGroup
import com.github.singularity.core.mdns.Node
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

class BroadcastViewModel(
    private val broadcastRepository: BroadcastRepository,
) : ViewModel() {

    private val syncGroups = broadcastRepository.syncGroups
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val requestedNodes = MutableStateFlow(emptyList<Node>())

    val uiState = requestedNodes.combine(syncGroups) { requestedNodes, syncGroups ->
        BroadcastUiState(
            syncGroups = syncGroups,
            defaultSyncGroup = syncGroups.first { group -> group.isDefault },
            requestedNodes = requestedNodes,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BroadcastUiState())

    fun execute(intent: BroadcastIntent) {
        when (intent) {
            is BroadcastIntent.Broadcast -> broadcast(intent.group)
            is BroadcastIntent.Approve -> approve(intent.node)
            is BroadcastIntent.CreateGroup -> create(intent.groupName)
            is BroadcastIntent.DeleteGroup -> delete(intent.hostedSyncGroup)
            is BroadcastIntent.NavBack -> Unit
        }
    }

    private fun broadcast(group: HostedSyncGroup) {
        broadcastRepository.stopBroadcast()
        broadcastRepository.broadcastGroup(group).onEach {
            requestedNodes.value = requestedNodes.value + it
        }.launchIn(viewModelScope)
    }

    private fun approve(node: Node) {
        broadcastRepository.approvePairRequest(node).onEach {
            if(it !is Resource.Loading) {
                requestedNodes.value = requestedNodes.value - node
            }
        }.launchIn(viewModelScope)
    }

    private fun create(groupName: String) {
        broadcastRepository.create(HostedSyncGroup(groupName)).launchIn(viewModelScope)
    }

    private fun delete(group: HostedSyncGroup) {
        broadcastRepository.delete(group).launchIn(viewModelScope)
    }

    override fun onCleared() {
        broadcastRepository.stopBroadcast()
        super.onCleared()
    }

}
