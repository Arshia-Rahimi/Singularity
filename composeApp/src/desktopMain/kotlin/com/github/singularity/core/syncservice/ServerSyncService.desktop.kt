package com.github.singularity.core.syncservice

import com.github.singularity.core.data.ClientConnectionRepository
import com.github.singularity.core.data.PreferencesRepository
import com.github.singularity.core.data.ServerConnectionRepository
import com.github.singularity.core.shared.SyncMode
import com.github.singularity.core.shared.util.next
import com.github.singularity.core.shared.util.stateInWhileSubscribed
import com.github.singularity.core.syncservice.events.PluginWrapper
import com.github.singularity.core.syncservice.events.SyncEventBridge
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ServerSyncService(
    private val preferencesRepo: PreferencesRepository,
    private val clientConnectionRepo: ClientConnectionRepository,
    private val serverConnectionRepo: ServerConnectionRepository,
    syncEventBridge: SyncEventBridge,
    pluginWrapper: PluginWrapper,
) : SyncService(
    syncEventBridge,
    pluginWrapper,
) {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override val syncMode = preferencesRepo.preferences.map { it.syncMode }
        .stateInWhileSubscribed(SyncMode.Client, scope)

    override val syncState: StateFlow<SyncState> = syncMode.flatMapLatest {
        if (it == SyncMode.Client) clientConnectionRepo.connectionState
        else serverConnectionRepo.runServer()
    }.stateInWhileSubscribed(ClientSyncState.NoDefaultServer, scope)

    override fun toggleSyncMode() {
        scope.launch {
            preferencesRepo.setSyncMode(syncMode.first().next())
        }
    }

    override fun refresh() {
        scope.launch {
            if (syncMode.first() == SyncMode.Client)
                clientConnectionRepo.refresh()
            else serverConnectionRepo.refresh()
        }
    }

}
