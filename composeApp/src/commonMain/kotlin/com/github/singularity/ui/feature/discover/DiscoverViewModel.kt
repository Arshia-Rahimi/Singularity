package com.github.singularity.ui.feature.discover

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

    private val servers = discoverRepo.discoveredServers.stateInWhileSubscribed(emptyList())

    private val pairRequestState = MutableStateFlow<PairRequestState>(PairRequestState.Idle)

    private var pairRequestJob: Job? = null

    val uiState = combine(servers, pairRequestState) { server, pairRequestState ->
        DiscoverUiState(
            servers = server,
            pairRequestState = pairRequestState,
        )
    }.stateInWhileSubscribed(DiscoverUiState())

    fun execute(intent: DiscoverIntent) {
        when (intent) {
            is DiscoverIntent.SendPairRequest -> sendPairRequest(intent.server)
            is DiscoverIntent.CancelPairRequest -> cancelPairRequest()
            else -> Unit
        }
    }

    private fun sendPairRequest(server: LocalServer) {
        pairRequestJob?.cancel()
        val job = discoverRepo.sendPairRequest(server).onEach {
            pairRequestState.value = when (it) {
                is Resource.Loading -> PairRequestState.Awaiting(server)
                is Resource.Error -> PairRequestState.Error(it.error?.message ?: "failed")
                is Resource.Success -> PairRequestState.Success
            }
        }.onCompletion {
            delay(5000)
            pairRequestState.value = PairRequestState.Idle
        }.launchIn(viewModelScope)
        pairRequestJob = job
    }

    private fun cancelPairRequest() {
        pairRequestJob?.cancel()
        pairRequestState.value = PairRequestState.Idle
    }

    override fun onCleared() {
        discoverRepo.release()
        super.onCleared()
    }

}
