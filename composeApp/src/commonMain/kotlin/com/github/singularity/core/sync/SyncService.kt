package com.github.singularity.core.sync

import com.github.singularity.core.data.ClientConnectionRepository
import com.github.singularity.core.data.SyncEventRepository
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

open class SyncService(
    private val clientConnectionRepo: ClientConnectionRepository,
    syncEventRepo: SyncEventRepository,
) {

    protected val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    open val syncMode = MutableStateFlow(SyncMode.Client).asStateFlow()

    open val connectionState: StateFlow<ConnectionState> = clientConnectionRepo.connectionState
        .stateInWhileSubscribed(ClientConnectionState.NoDefaultServer, scope)

    init {
        PluginManager(scope, syncEventRepo)
    }

    open fun toggleSyncMode() = Unit

    open fun refreshClient() {
        clientConnectionRepo.refresh()
    }

}
