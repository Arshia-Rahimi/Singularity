package com.github.singularity.ui.feature.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.singularity.core.data.DiscoverRepository
import com.github.singularity.core.shared.model.LocalServer
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

class DiscoverViewModel(
    private val discoverRepo: DiscoverRepository,
) : ViewModel() {

    private val servers = discoverRepo.discoveredServers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val uiState = combine(servers) { server ->
        DiscoverUiState(
            servers = server[0],
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DiscoverUiState())

    fun execute(intent: DiscoverIntent) {
        when (intent) {
            is DiscoverIntent.SendPairRequest -> sendPairRequest(intent.server)
            else -> Unit
        }
    }

    private fun sendPairRequest(server: LocalServer) {
        discoverRepo.sendPairRequest(server).onEach {
            // todo
        }.launchIn(viewModelScope)
    }

    override fun onCleared() {
        discoverRepo.release()
        super.onCleared()
    }

}
