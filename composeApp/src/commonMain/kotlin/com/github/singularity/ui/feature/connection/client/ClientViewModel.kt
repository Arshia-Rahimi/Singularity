package com.github.singularity.ui.feature.connection.client

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.singularity.core.data.DiscoverRepository
import com.github.singularity.core.data.JoinedSyncGroupRepository
import com.github.singularity.core.shared.util.stateInWhileSubscribed
import com.github.singularity.core.syncservice.ClientSyncState
import com.github.singularity.core.syncservice.SyncService
import com.github.singularity.ui.feature.connection.client.pages.index.PairRequestState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ClientViewModel(
    private val discoverRepo: DiscoverRepository,
    private val joinedSyncGroupRepo: JoinedSyncGroupRepository,
    private val syncService: SyncService,
) : ViewModel() {

    private val connectionState = syncService.syncState
        .filterIsInstance<ClientSyncState>()
        .stateInWhileSubscribed(ClientSyncState.Loading)

    private val combinedDiscoveredAndJoinedSyncGroupsFlow = combine(
        discoverRepo.discoveredServers,
        joinedSyncGroupRepo.joinedSyncGroups,
    ) { discoveredServers, joinedSyncGroups ->
        discoveredServers.map { it.toDiscoveredServer() }.distinctBy { it.groupId } to
                joinedSyncGroups.map { it.toPairedSyncGroup() }.distinctBy { it.groupId }
    }.stateInWhileSubscribed(emptyList<DiscoveredServer>() to emptyList())

    private val availableServers = combinedDiscoveredAndJoinedSyncGroupsFlow
        .map { (discoveredServers, joinedSyncGroups) ->
            discoveredServers.filter {
                it.groupId !in joinedSyncGroups.map { j -> j.groupId }
            }
        }.stateInWhileSubscribed(emptyList())

    private val joinedSyncGroups = combinedDiscoveredAndJoinedSyncGroupsFlow
        .map { (discoveredServers, joinedSyncGroups) ->
            joinedSyncGroups.map { joinedGroup ->
                val isAvailable =
                    discoveredServers.find { joinedGroup.groupId == it.groupId } != null
                joinedGroup.copy(isAvailable = isAvailable)
            }
        }.stateInWhileSubscribed(emptyList())

    private val sentPairRequestState = MutableStateFlow<PairRequestState>(PairRequestState.Idle)

    val uiState = combine(
        connectionState,
        availableServers,
        sentPairRequestState,
        joinedSyncGroups,
        ::ClientUiState,
    ).stateInWhileSubscribed(ClientUiState())

    private var pairRequestJob: Job? = null

    fun execute(intent: ClientIntent) = when (intent) {
        is ClientIntent.SendPairRequest -> sendPairRequest(intent.server)
        is ClientIntent.CancelPairRequest -> cancelPairRequest()
        is ClientIntent.DeleteGroup -> delete(intent.group)
        is ClientIntent.SetAsDefault -> setAsDefault(intent.group)
        is ClientIntent.RefreshConnection -> syncService.refresh()
        is ClientIntent.ToIndex -> toIndex()
    }

    private fun sendPairRequest(server: DiscoveredServer) {
        pairRequestJob?.cancel()
        viewModelScope.launch {
            sentPairRequestState.value = PairRequestState.Awaiting(server)
            try {
                discoverRepo.sendPairRequest(server.toLocalServer())
                sentPairRequestState.value = PairRequestState.Success(server)
            } catch (e: Exception) {
                sentPairRequestState.value = PairRequestState.Error(e.message ?: "failed")
            }

            delay(5000)
            sentPairRequestState.value = PairRequestState.Idle
        }
    }

    private fun cancelPairRequest() {
        pairRequestJob?.cancel()
        sentPairRequestState.value = PairRequestState.Idle
    }

    private fun delete(group: PairedSyncGroup) {
        viewModelScope.launch { joinedSyncGroupRepo.delete(group.groupId) }
    }

    private fun setAsDefault(group: PairedSyncGroup) {
        viewModelScope.launch {
            joinedSyncGroupRepo.setAsDefault(group.groupId)
        }
    }

    private fun toIndex() {
        viewModelScope.launch {
            discoverRepo.removeAllDefaults()
        }
    }

}
