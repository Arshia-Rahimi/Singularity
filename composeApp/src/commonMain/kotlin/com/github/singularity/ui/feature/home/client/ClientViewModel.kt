package com.github.singularity.ui.feature.home.client

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.singularity.app.navigation.components.AppNavigationController
import com.github.singularity.core.data.DiscoverRepository
import com.github.singularity.core.data.JoinedSyncGroupRepository
import com.github.singularity.core.shared.model.ClientConnectionState
import com.github.singularity.core.shared.model.JoinedSyncGroup
import com.github.singularity.core.shared.model.LocalServer
import com.github.singularity.core.shared.util.Resource
import com.github.singularity.core.shared.util.stateInWhileSubscribed
import com.github.singularity.core.sync.SyncService
import com.github.singularity.ui.feature.home.client.components.PairRequestState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalTime::class)
class ClientViewModel(
    private val syncService: SyncService,
    private val discoverRepo: DiscoverRepository,
    private val joinedSyncGroupRepo: JoinedSyncGroupRepository,
) : ViewModel() {

    private val connectionState = syncService.connectionState
        .filterIsInstance<ClientConnectionState>()

    private val availableServers = discoverRepo.discoveredServers
        .stateInWhileSubscribed(emptyList())

    private val sentPairRequestState = MutableStateFlow<PairRequestState>(PairRequestState.Idle)

    private val joinedSyncGroups = joinedSyncGroupRepo.joinedSyncGroups
        .stateInWhileSubscribed(emptyList())

    private val defaultSyncGroup = joinedSyncGroupRepo.defaultJoinedSyncGroup
        .stateInWhileSubscribed(null)

    val uiState = combine(
        connectionState,
        availableServers,
        sentPairRequestState,
        joinedSyncGroups,
        defaultSyncGroup,
    ) { connectionState, availableServers, sentPairRequestState, joinedSyncGroups, defaultSyncGroup ->
        ClientUiState(
            connectionState = connectionState,
            availableServers = availableServers.toMutableStateList(),
            joinedSyncGroups = joinedSyncGroups.toMutableStateList(),
            sentPairRequestState = sentPairRequestState,
            defaultSyncGroup = defaultSyncGroup,
        )
    }.stateInWhileSubscribed(ClientUiState())

    private var pairRequestJob: Job? = null

    private fun refreshConnection() {
        syncService.refresh()
    }

    fun execute(intent: ClientIntent) {
        when (intent) {
            is ClientIntent.SendPairRequest -> sendPairRequest(intent.server)
            is ClientIntent.CancelPairRequest -> cancelPairRequest()
            is ClientIntent.RefreshDiscovery -> refreshDiscovery()
            is ClientIntent.RefreshConnection -> refreshConnection()
            is ClientIntent.ToggleSyncMode -> syncService.toggleSyncMode()
            is ClientIntent.DeleteGroup -> delete(intent.group)
            is ClientIntent.SetAsDefault -> setAsDefault(intent.group)
            is ClientIntent.OpenDrawer -> AppNavigationController.toggleDrawer()
            is ClientIntent.RemoveAllDefaults -> removeAllDefaults()
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
        }
    }

    private fun removeAllDefaults() {
        viewModelScope.launch {
            joinedSyncGroupRepo.removeAllDefaults()
        }
    }

    private fun refreshDiscovery() {
        viewModelScope.launch {
            discoverRepo.refreshDiscovery()
        }
    }

}
