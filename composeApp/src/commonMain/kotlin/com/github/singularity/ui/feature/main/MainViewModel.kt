package com.github.singularity.ui.feature.main

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.singularity.core.data.BroadcastRepository
import com.github.singularity.core.data.DiscoverRepository
import com.github.singularity.core.data.SyncEventBridge
import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.core.shared.model.LocalServer
import com.github.singularity.core.shared.model.Node
import com.github.singularity.core.shared.model.websocket.SyncEvent
import com.github.singularity.core.shared.util.Resource
import com.github.singularity.core.shared.util.stateInWhileSubscribed
import com.github.singularity.core.sync.ClientSyncService
import com.github.singularity.ui.feature.main.components.discover.PairRequestState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
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
    private val syncService: ClientSyncService,
    private val broadcastRepo: BroadcastRepository,
    private val discoverRepo: DiscoverRepository,
    // test
    private val syncEventBridge: SyncEventBridge,
    // test end
) : ViewModel() {

    // test
    fun sendEvent() {
        viewModelScope.launch {
            syncEventBridge.send(TestEvent(Clock.System.now().toEpochMilliseconds(), "test"))
        }
    }
    // test end

    private val connectionState = syncService.connectionState

    // broadcast
    private val hostedSyncGroups = broadcastRepo.syncGroups
        .stateInWhileSubscribed(emptyList())

    private val receivedPairRequests = broadcastRepo.pairRequests
        .stateInWhileSubscribed(emptyList())

    private val broadcastUiState = combine(
        hostedSyncGroups,
        receivedPairRequests,
    ) { hostedSyncGroups, receivedPairRequests ->
        BroadcastUiState(
            hostedSyncGroups = hostedSyncGroups.toMutableStateList(),
            receivedPairRequests = receivedPairRequests.toMutableStateList(),
        )
    }

    // discover
    private val availableServers = discoverRepo.discoveredServers
        .stateInWhileSubscribed(emptyList())

    private val sentPairRequestState = MutableStateFlow<PairRequestState>(PairRequestState.Idle)

    private val discoverUiState = combine(
        availableServers,
        sentPairRequestState,
    ) { availableServers, sentPairRequestState ->
        DiscoverUiState(
            availableServers = availableServers.toMutableStateList(),
            sentPairRequestState = sentPairRequestState,
        )
    }

    private var pairRequestJob: Job? = null

    val uiState = combine(
        connectionState,
        syncService.syncMode,
        discoverUiState,
        broadcastUiState,
    ) { connectionState, syncMode, discoverUiState, broadcastUiState ->
        MainUiState(
            connectionState = connectionState,
            syncMode = syncMode,
            discoverUiState = discoverUiState,
            broadcastUiState = broadcastUiState,
        )
    }.stateInWhileSubscribed(MainUiState())

    fun execute(intent: MainIntent) {
        when (intent) {
            is MainIntent.RefreshConnection -> refreshConnection()
            is MainIntent.ToggleSyncMode -> toggleSyncMode()
            is MainIntent.BroadcastIntent -> executeBroadcast(intent)
            is MainIntent.DiscoverIntent -> executeDiscover(intent)
            is MainIntent.ToSettingsScreen -> Unit
        }
    }

    private fun toggleSyncMode() {
        viewModelScope.launch { syncService.toggleSyncMode() }
    }

    private fun refreshConnection() {
        syncService.refreshClient()
    }

    fun executeBroadcast(intent: MainIntent.BroadcastIntent) {
        when (intent) {
            is MainIntent.BroadcastIntent.Broadcast -> broadcast()
            is MainIntent.BroadcastIntent.StopBroadcast -> stopBroadcast()
            is MainIntent.BroadcastIntent.Approve -> approve(intent.node)
            is MainIntent.BroadcastIntent.Reject -> reject(intent.node)
            is MainIntent.BroadcastIntent.CreateGroup -> create(intent.groupName)
            is MainIntent.BroadcastIntent.EditGroupName -> editName(intent.groupName, intent.group)
            is MainIntent.BroadcastIntent.DeleteGroup -> delete(intent.group)
            is MainIntent.BroadcastIntent.SetAsDefault -> setAsDefault(intent.group)
        }
    }

    private fun broadcast() {
        viewModelScope.launch {
            broadcastRepo.stopBroadcast()
            broadcastRepo.startBroadcast()
        }
    }

    private fun stopBroadcast() {
        viewModelScope.launch {
            broadcastRepo.stopBroadcast()
        }
    }

    private fun setAsDefault(group: HostedSyncGroup) {
        viewModelScope.launch {
            broadcastRepo.setAsDefault(group)
        }
    }

    private fun approve(node: Node) {
        broadcastRepo.approvePairRequest(node)
    }

    private fun reject(node: Node) {
        broadcastRepo.rejectPairRequest(node)
    }

    private fun create(groupName: String) {
        broadcastRepo.create(HostedSyncGroup(groupName)).launchIn(viewModelScope)
    }

    private fun editName(groupName: String, group: HostedSyncGroup) {
        broadcastRepo.editName(groupName, group).launchIn(viewModelScope)
    }

    private fun delete(group: HostedSyncGroup) {
        broadcastRepo.delete(group).launchIn(viewModelScope)
    }

    override fun onCleared() {
        viewModelScope.launch {
            broadcastRepo.stopBroadcast()
            super.onCleared()
        }
    }

    private fun executeDiscover(intent: MainIntent.DiscoverIntent) {
        when (intent) {
            is MainIntent.DiscoverIntent.SendPairRequest -> sendPairRequest(intent.server)
            is MainIntent.DiscoverIntent.CancelPairRequest -> cancelPairRequest()
            is MainIntent.DiscoverIntent.RefreshDiscovery -> discoverRepo.refreshDiscovery()
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

}
