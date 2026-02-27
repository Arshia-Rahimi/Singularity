package com.github.singularity.core.syncservice

import com.github.singularity.core.shared.SyncMode
import com.github.singularity.core.syncservice.events.SyncEventBridge
import com.github.singularity.core.syncservice.plugin.PluginEventHandler
import com.github.singularity.core.syncservice.plugin.PluginEventHandlerImpl
import com.github.singularity.core.syncservice.plugin.PluginWrapper
import kotlinx.coroutines.flow.StateFlow

abstract class SyncService(
    syncEventBridge: SyncEventBridge,
    pluginWrapper: PluginWrapper,
): PluginEventHandler by PluginEventHandlerImpl(
    syncEventBridge,
    pluginWrapper,
) {

    abstract val syncMode: StateFlow<SyncMode>

    abstract val syncState: StateFlow<SyncState>

    abstract fun toggleSyncMode()

    abstract fun refresh()

}
