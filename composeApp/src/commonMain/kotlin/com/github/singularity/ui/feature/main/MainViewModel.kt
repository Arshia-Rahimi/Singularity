package com.github.singularity.ui.feature.main

import androidx.lifecycle.ViewModel
import com.github.singularity.core.data.ClientConnectionRepository
import com.github.singularity.core.shared.model.ConnectionState
import com.github.singularity.core.shared.util.stateInWhileSubscribed
import kotlinx.coroutines.flow.combine

class MainViewModel(
    private val connectionRepo: ClientConnectionRepository,
) : ViewModel() {

    private val connectionState = connectionRepo.connectionState
        .stateInWhileSubscribed(ConnectionState.NoDefaultServer)

    val uiState = combine(connectionState) { connectionState ->
        MainUiState(
            connectionState = connectionState[0],
        )
    }.stateInWhileSubscribed(MainUiState())

    fun execute(intent: MainIntent) {
        when (intent) {
            is MainIntent.RefreshConnection -> refreshConnection()
            else -> Unit
        }
    }

    private fun refreshConnection() = connectionRepo.refresh()

}
