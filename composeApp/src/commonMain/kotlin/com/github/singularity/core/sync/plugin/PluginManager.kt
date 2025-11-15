package com.github.singularity.core.sync.plugin

import com.github.singularity.core.sync.SyncEventBridge
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

interface PluginManager

class PluginManagerImpl(
    private val plugins: List<Plugin>,
    syncEventBridge: SyncEventBridge,
) : PluginManager {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    init {
        syncEventBridge.incomingSyncEvents.onEach { event ->
            plugins.firstOrNull { plugin ->
                plugin.pluginName == event.pluginName
            }?.handleEvent(event.data)
        }.launchIn(scope)
    }

}
