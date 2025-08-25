package com.github.singularity.ui.feature.broadcast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.singularity.core.data.BroadcastRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class BroadcastViewModel(
    private val broadcastRepository: BroadcastRepository,
) : ViewModel() {

    private val syncGroups = broadcastRepository.syncGroups
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val uiState = combine(syncGroups) {
        BroadcastUiState(
            syncGroups = it[0],
            defaultSyncGroup = it[0].first { group -> group.isDefault },
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BroadcastUiState())

    fun execute(intent: BroadcastIntent) {
        when (intent) {
            else -> Unit
        }
    }

}
