package com.github.singularity.ui.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.singularity.core.data.SyncEventRepository
import com.github.singularity.core.shared.model.websocket.SyncEvent
import com.github.singularity.core.shared.util.stateInWhileSubscribed
import com.github.singularity.core.sync.SyncService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

// test
@Serializable
@SerialName("test")
data class TestEvent(
    val time: Long,
    override val plugin: String
) : SyncEvent
// test end

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalTime::class)
class MainViewModel(
    private val syncService: SyncService,
    // test
    private val syncEventRepo: SyncEventRepository,
    // test end
) : ViewModel() {

    // test
    val events = syncEventRepo.incomingSyncEvents
        .runningFold(emptyList<TestEvent>()) { events, newEvent ->
            if (newEvent is TestEvent) events + newEvent
            else events
        }.stateInWhileSubscribed(emptyList())

    fun sendEvent() {
        viewModelScope.launch {
            syncEventRepo.send(TestEvent(Clock.System.now().toEpochMilliseconds(), "test"))
        }
    }
    // test end

    private val connectionState = syncService.connectionState

    val uiState = combine(
        connectionState,
        syncService.syncMode,
    ) { connectionState, syncMode ->
        MainUiState(
            connectionState = connectionState,
            syncMode = syncMode,
        )
    }.stateInWhileSubscribed(MainUiState())

    fun execute(intent: MainIntent) {
        when (intent) {
            is MainIntent.RefreshConnection -> refreshConnection()
            is MainIntent.ToggleSyncMode -> toggleSyncMode()
            is MainIntent.ToDiscoverScreen -> Unit
            is MainIntent.ToSettingsScreen -> Unit
            is MainIntent.ToBroadcastScreen -> Unit
        }
    }

    private fun toggleSyncMode() {
        viewModelScope.launch { syncService.toggleSyncMode() }
    }

    private fun refreshConnection() {
        syncService.refreshClient()
    }

}
