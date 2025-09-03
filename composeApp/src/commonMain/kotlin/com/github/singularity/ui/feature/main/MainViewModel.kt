package com.github.singularity.ui.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.singularity.core.data.ConnectionRepository
import com.github.singularity.core.shared.model.ConnectionState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class MainViewModel(
    private val connectionRepo: ConnectionRepository,
) : ViewModel() {

    private val connectionState = connectionRepo.connectionState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ConnectionState.NoDefaultServer,
        )

    val uiState = combine(connectionState) { connectionState ->
        MainUiState(
            connectionState = connectionState[0],
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MainUiState())

    fun execute(intent: MainIntent) {
        when (intent) {
            is MainIntent.RefreshConnection -> refreshConnection()
            else -> Unit
        }
    }

    private fun refreshConnection() = connectionRepo.refresh()

}
