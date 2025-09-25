package com.github.singularity.core.sync

import com.github.singularity.core.data.ClientConnectionRepository
import com.github.singularity.core.data.SyncEventBridge
import com.github.singularity.core.shared.SyncMode
import com.github.singularity.core.shared.model.ClientConnectionState
import com.github.singularity.core.shared.model.ConnectionState
import com.github.singularity.core.shared.util.stateInWhileSubscribed
import com.github.singularity.core.sync.plugin.PluginManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

open class ClientSyncService(
    private val clientConnectionRepo: ClientConnectionRepository,
    syncEventBridge: SyncEventBridge,
) : SyncService {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val pluginManager = PluginManager(scope, syncEventBridge)

    override val syncMode = MutableStateFlow(SyncMode.Client).asStateFlow()

    override val connectionState: StateFlow<ConnectionState> = clientConnectionRepo.connectionState
        .stateInWhileSubscribed(ClientConnectionState.NoDefaultServer, scope)

    override fun toggleSyncMode() = Unit

    override fun refreshClient() {
        clientConnectionRepo.refresh()
    }

}
