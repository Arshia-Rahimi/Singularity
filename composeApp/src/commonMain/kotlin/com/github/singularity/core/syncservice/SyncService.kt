package com.github.singularity.core.syncservice

import com.github.singularity.core.data.PluginSettingsRepository
import com.github.singularity.core.shared.SyncMode
import com.github.singularity.core.syncservice.plugin.PluginEventHandler
import com.github.singularity.core.syncservice.plugin.PluginEventHandlerImpl
import com.github.singularity.core.syncservice.plugin.PluginWrapper
import kotlinx.coroutines.flow.StateFlow

abstract class SyncService(
    pluginSettingsRepo: PluginSettingsRepository,
    syncEventBridge: SyncEventBridge,
    pluginWrapper: PluginWrapper,
): PluginEventHandler by PluginEventHandlerImpl(
    pluginSettingsRepo,
    syncEventBridge,
    pluginWrapper,
) {

    abstract val syncMode: StateFlow<SyncMode>

    abstract val syncState: StateFlow<SyncState>

    abstract fun toggleSyncMode()

    abstract fun refresh()

}
