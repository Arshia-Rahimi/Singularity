package com.github.singularity.core.sync.plugin

import com.github.singularity.core.data.SyncEventRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

open class PluginManager(
    private val scope: CoroutineScope,
    private val syncEventRepo: SyncEventRepository,
) {

    val plugins: List<Plugin> = PluginsList.map { plugin ->
        plugin { event ->
            scope.launch { syncEventRepo.send(event) }
        }
    }

    init {
        syncEventRepo.incomingSyncEvents.onEach { event ->
            plugins.firstOrNull { plugin ->
                plugin.pluginName == event.plugin
            }?.handleEvent(event)
        }.launchIn(scope)
    }

}
