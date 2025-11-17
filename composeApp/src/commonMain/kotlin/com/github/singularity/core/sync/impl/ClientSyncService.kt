package com.github.singularity.core.sync.impl

import com.github.singularity.core.data.ClientConnectionRepository
import com.github.singularity.core.datasource.memory.SyncEventBridge
import com.github.singularity.core.shared.SyncMode
import com.github.singularity.core.shared.model.ClientSyncState
import com.github.singularity.core.shared.model.SyncState
import com.github.singularity.core.shared.util.stateInWhileSubscribed
import com.github.singularity.core.sync.SyncService
import com.github.singularity.core.sync.plugin.Plugin
import com.github.singularity.core.sync.plugin.PluginManager
import com.github.singularity.core.sync.plugin.PluginManagerImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ClientSyncService(
    private val clientConnectionRepo: ClientConnectionRepository,
    plugins: List<Plugin>,
    syncEventBridge: SyncEventBridge,
) : SyncService,
    PluginManager by PluginManagerImpl(plugins, syncEventBridge) {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override val syncMode = MutableStateFlow(SyncMode.Client).asStateFlow()

    override val syncState: StateFlow<SyncState> = clientConnectionRepo.connectionState
        .stateInWhileSubscribed(ClientSyncState.NoDefaultServer, scope)

    override fun toggleSyncMode() = Unit

    override fun refresh() {
        scope.launch {
            clientConnectionRepo.refresh()
        }
    }

}
