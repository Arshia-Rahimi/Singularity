package com.github.singularity.ui.feature.broadcast

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.singularity.core.data.BroadcastRepository
import com.github.singularity.core.database.entities.HostedSyncGroup
import com.github.singularity.core.shared.model.Node
import com.github.singularity.core.shared.util.Resource
import com.github.singularity.core.shared.util.stateInWhileSubscribed
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class BroadcastViewModel(
    private val broadcastRepo: BroadcastRepository,
) : ViewModel() {

    private val syncGroups = broadcastRepo.syncGroups
        .map { it.sortedWith(compareBy<HostedSyncGroup> { group -> !group.isDefault }.thenBy { group -> group.name }) }
        .stateInWhileSubscribed(emptyList())

    private val requestedNodes = MutableStateFlow(emptyList<Node>())

    private val isBroadcasting = MutableStateFlow(false)

    @OptIn(FlowPreview::class)
    val uiState = combine(
        requestedNodes,
        syncGroups,
        isBroadcasting,
    ) { requestedNodes, syncGroups, isBroadcasting ->
        BroadcastUiState(
            syncGroups = syncGroups.toMutableStateList(),
            requestedNodes = requestedNodes,
            isBroadcasting = isBroadcasting,
        )
    }.debounce(10).stateInWhileSubscribed(BroadcastUiState())

    fun execute(intent: BroadcastIntent) {
        when (intent) {
            is BroadcastIntent.ToggleBroadcast -> if (uiState.value.isBroadcasting) stopBroadcast() else broadcast()
            is BroadcastIntent.Approve -> approve(intent.node)
            is BroadcastIntent.CreateGroup -> create(intent.groupName)
            is BroadcastIntent.EditGroupName -> editName(intent.groupName, intent.group)
            is BroadcastIntent.DeleteGroup -> delete(intent.group)
            is BroadcastIntent.SetAsDefault -> setAsDefault(intent.group)
            is BroadcastIntent.NavBack -> Unit
        }
    }

    private fun broadcast() {
        broadcastRepo.stopBroadcast()
        val defaultGroup = syncGroups.value.firstOrNull { it.isDefault } ?: return

        requestedNodes.value = emptyList()
        isBroadcasting.value = true
        broadcastRepo.broadcastGroup(defaultGroup).onEach {
            requestedNodes.value = requestedNodes.value + it
        }.launchIn(viewModelScope)
    }

    private fun stopBroadcast() {
        broadcastRepo.stopBroadcast()
        requestedNodes.value = emptyList()
        isBroadcasting.value = false
    }

    private fun approve(node: Node) {
        broadcastRepo.approvePairRequest(node).onEach {
            when (it) {
                is Resource.Loading -> Unit
                is Resource.Error -> Unit // todo: show error
                is Resource.Success -> requestedNodes.value = requestedNodes.value - node
            }
        }.launchIn(viewModelScope)
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

    private fun setAsDefault(group: HostedSyncGroup) {
        viewModelScope.launch { broadcastRepo.setAsDefault(group) }
    }

    override fun onCleared() {
        broadcastRepo.stopBroadcast()
        super.onCleared()
    }

}
