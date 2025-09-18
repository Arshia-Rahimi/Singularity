package com.github.singularity.ui.feature.discover

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.singularity.core.data.DiscoverRepository
import com.github.singularity.core.shared.model.LocalServer
import com.github.singularity.core.shared.util.Resource
import com.github.singularity.core.shared.util.stateInWhileSubscribed
import com.github.singularity.ui.feature.discover.components.PairRequestState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

class DiscoverViewModel(
    private val discoverRepo: DiscoverRepository,
) : ViewModel() {

    private val servers = discoverRepo.discoveredServers
        .stateInWhileSubscribed(emptyList())

    private val pairRequestState = MutableStateFlow<PairRequestState>(PairRequestState.Idle)

    val uiState = combine(servers, pairRequestState) { server, pairRequestState ->
        DiscoverUiState(
            servers = server.toMutableStateList(),
            pairRequestState = pairRequestState,
        )
    }.stateInWhileSubscribed(DiscoverUiState())

    private var pairRequestJob: Job? = null

    fun execute(intent: DiscoverIntent) {
        when (intent) {
            is DiscoverIntent.SendPairRequest -> sendPairRequest(intent.server)
            is DiscoverIntent.CancelPairRequest -> cancelPairRequest()
            is DiscoverIntent.NavBack -> Unit
            is DiscoverIntent.RefreshDiscovery -> discoverRepo.refreshDiscovery()
        }
    }

    private fun sendPairRequest(server: LocalServer) {
        pairRequestJob?.cancel()
        pairRequestJob = discoverRepo.sendPairRequest(server).onEach {
            pairRequestState.value = when (it) {
                is Resource.Loading -> PairRequestState.Awaiting(server)
                is Resource.Error -> PairRequestState.Error(it.error?.message ?: "failed")
                is Resource.Success -> PairRequestState.Success
            }
        }.onCompletion {
            delay(5000)
            pairRequestState.value = PairRequestState.Idle
        }.launchIn(viewModelScope)
    }

    private fun cancelPairRequest() {
        pairRequestJob?.cancel()
        pairRequestState.value = PairRequestState.Idle
    }

}
