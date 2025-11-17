package com.github.singularity.ui.feature.connection.client

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.singularity.core.data.DiscoverRepository
import com.github.singularity.core.data.JoinedSyncGroupRepository
import com.github.singularity.core.shared.model.ClientSyncState
import com.github.singularity.core.shared.model.JoinedSyncGroup
import com.github.singularity.core.shared.model.LocalServer
import com.github.singularity.core.shared.util.Resource
import com.github.singularity.core.shared.util.stateInWhileSubscribed
import com.github.singularity.core.sync.SyncService
import com.github.singularity.ui.feature.connection.client.pages.index.PairRequestState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ClientViewModel(
    private val discoverRepo: DiscoverRepository,
    private val joinedSyncGroupRepo: JoinedSyncGroupRepository,
    private val syncService: SyncService,
) : ViewModel() {

    private val connectionState = syncService.syncState
        .filterIsInstance<ClientSyncState>()
        .stateInWhileSubscribed(ClientSyncState.Loading)

    private val shouldDiscover = MutableStateFlow(false)

    private val availableServers = shouldDiscover.flatMapLatest {
        if (it) discoverRepo.discoveredServers
        else flowOf(null)
    }.stateInWhileSubscribed(null)

    private val sentPairRequestState = MutableStateFlow<PairRequestState>(PairRequestState.Idle)

    private val joinedSyncGroups =
        joinedSyncGroupRepo.joinedSyncGroups.stateInWhileSubscribed(emptyList())

    val uiState = combine(
        availableServers,
        sentPairRequestState,
        joinedSyncGroups,
	    connectionState,
    ) { availableServers, sentPairRequestState, joinedSyncGroups, connectionState ->
        ClientUiState(
	        connectionState = connectionState,
	        availableServers = availableServers?.distinctBy { it.syncGroupId }?.toMutableStateList()
		        ?: mutableStateListOf(),
	        joinedSyncGroups = joinedSyncGroups.distinctBy { it.syncGroupId }.toMutableStateList(),
            sentPairRequestState = sentPairRequestState,
            isDiscovering = availableServers != null,
        )
    }.stateInWhileSubscribed(ClientUiState())

    private var pairRequestJob: Job? = null

    fun execute(intent: ClientIntent) {
        when (intent) {
            is ClientIntent.SendPairRequest -> sendPairRequest(intent.server)
            is ClientIntent.CancelPairRequest -> cancelPairRequest()
            is ClientIntent.DeleteGroup -> delete(intent.group)
            is ClientIntent.SetAsDefault -> setAsDefault(intent.group)
            is ClientIntent.StartDiscovery -> shouldDiscover.value = true
            is ClientIntent.StopDiscovery -> shouldDiscover.value = false
	        is ClientIntent.RefreshConnection -> syncService.refresh()
            is ClientIntent.ToIndex -> toIndex()
        }
    }

    private fun sendPairRequest(server: LocalServer) {
        pairRequestJob?.cancel()
        pairRequestJob = discoverRepo.sendPairRequest(server).onEach {
            sentPairRequestState.value = when (it) {
                is Resource.Loading -> PairRequestState.Awaiting(server)
                is Resource.Error -> PairRequestState.Error(it.error?.message ?: "failed")
                is Resource.Success -> PairRequestState.Success(server)
            }
        }.onCompletion {
            delay(5000)
            sentPairRequestState.value = PairRequestState.Idle
        }.launchIn(viewModelScope)
    }

    private fun cancelPairRequest() {
        pairRequestJob?.cancel()
        sentPairRequestState.value = PairRequestState.Idle
    }

    private fun delete(group: JoinedSyncGroup) {
        viewModelScope.launch { joinedSyncGroupRepo.delete(group) }
    }

    private fun setAsDefault(group: JoinedSyncGroup) {
        viewModelScope.launch {
            joinedSyncGroupRepo.setAsDefault(group)
            shouldDiscover.value = false
        }
    }

    private fun toIndex() {
        viewModelScope.launch {
            discoverRepo.removeAllDefaults()
        }
    }

}
