package com.github.singularity.core.sync

import com.github.singularity.core.data.ClientConnectionRepository
import com.github.singularity.core.data.PreferencesRepository
import com.github.singularity.core.data.ServerConnectionRepository
import com.github.singularity.core.data.SyncEventRepository
import com.github.singularity.core.shared.SyncMode
import com.github.singularity.core.shared.model.ClientConnectionState
import com.github.singularity.core.shared.model.ConnectionState
import com.github.singularity.core.shared.util.next
import com.github.singularity.core.shared.util.stateInWhileSubscribed
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ServerSyncService(
    private val preferencesRepo: PreferencesRepository,
    serverConnectionRepo: ServerConnectionRepository,
    clientConnectionRepo: ClientConnectionRepository,
    syncEventRepo: SyncEventRepository,
) : SyncService(
    syncEventRepo = syncEventRepo,
    clientConnectionRepo = clientConnectionRepo,
) {

    override val syncMode = preferencesRepo.preferences.map { it.syncMode }
        .stateInWhileSubscribed(SyncMode.Client, scope)

    override val connectionState: StateFlow<ConnectionState> = syncMode.flatMapLatest {
        if (it == SyncMode.Client) clientConnectionRepo.connectionState
        else serverConnectionRepo.runServer()
    }.stateInWhileSubscribed(ClientConnectionState.NoDefaultServer, scope)

    override fun toggleSyncMode() {
        scope.launch {
            preferencesRepo.setSyncMode(syncMode.first().next())
        }
    }

    override fun refreshClient() {
        scope.launch {
            if (syncMode.first() == SyncMode.Client) super.refreshClient()
        }
    }

}
