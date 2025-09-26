package com.github.singularity.core.sync.plugin

import com.github.singularity.core.data.SyncEventBridge
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

interface PluginManager

class PluginManagerImpl(
    private val syncEventBridge: SyncEventBridge,
) : PluginManager {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    val plugins: List<Plugin> = PluginsList.map { plugin ->
        plugin { event ->
            scope.launch { syncEventBridge.send(event) }
        }
    }

    init {
        syncEventBridge.incomingSyncEvents.onEach { event ->
            plugins.firstOrNull { plugin ->
                plugin.pluginName == event.plugin
            }?.handleEvent(event)
        }.launchIn(scope)
    }

}
