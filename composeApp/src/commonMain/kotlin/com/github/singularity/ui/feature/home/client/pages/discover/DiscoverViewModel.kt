package com.github.singularity.ui.feature.home.client.pages.discover

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.singularity.core.data.DiscoverRepository
import com.github.singularity.core.data.JoinedSyncGroupRepository
import com.github.singularity.core.shared.model.JoinedSyncGroup
import com.github.singularity.core.shared.model.LocalServer
import com.github.singularity.core.shared.util.Resource
import com.github.singularity.core.shared.util.stateInWhileSubscribed
import com.github.singularity.ui.feature.home.client.pages.discover.components.PairRequestState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalTime::class)
class DiscoverViewModel(
    private val discoverRepo: DiscoverRepository,
    private val joinedSyncGroupRepo: JoinedSyncGroupRepository,
) : ViewModel() {

    private val shouldDiscover = MutableStateFlow(false)

    private val availableServers = shouldDiscover.flatMapLatest {
        if (it) discoverRepo.discoveredServers
        else flowOf(null)
    }.stateInWhileSubscribed(emptyList())

    private val sentPairRequestState = MutableStateFlow<PairRequestState>(PairRequestState.Idle)

    private val joinedSyncGroups =
        joinedSyncGroupRepo.joinedSyncGroups.stateInWhileSubscribed(emptyList())

    private val defaultSyncGroup =
        joinedSyncGroupRepo.defaultJoinedSyncGroup.stateInWhileSubscribed(null)

    val uiState = combine(
        availableServers,
        sentPairRequestState,
        joinedSyncGroups,
        defaultSyncGroup,
    ) { availableServers, sentPairRequestState, joinedSyncGroups, defaultSyncGroup ->
        DiscoverUiState(
            availableServers = availableServers?.toMutableStateList(),
            joinedSyncGroups = joinedSyncGroups.toMutableStateList(),
            sentPairRequestState = sentPairRequestState,
            defaultSyncGroup = defaultSyncGroup,
        )
    }.stateInWhileSubscribed(DiscoverUiState())

    private var pairRequestJob: Job? = null

    fun execute(intent: DiscoverIntent) {
        when (intent) {
            is DiscoverIntent.SendPairRequest -> sendPairRequest(intent.server)
            is DiscoverIntent.CancelPairRequest -> cancelPairRequest()
            is DiscoverIntent.RefreshDiscovery -> refreshDiscovery()
            is DiscoverIntent.DeleteGroup -> delete(intent.group)
            is DiscoverIntent.SetAsDefault -> setAsDefault(intent.group)
            is DiscoverIntent.RemoveAllDefaults -> removeAllDefaults()
            is DiscoverIntent.StartDiscovery -> startDiscovery()
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

    private fun startDiscovery() {
        shouldDiscover.value = true
    }

}
