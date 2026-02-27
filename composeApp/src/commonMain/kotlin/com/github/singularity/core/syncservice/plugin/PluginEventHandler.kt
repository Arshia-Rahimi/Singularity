package com.github.singularity.core.syncservice.plugin

import com.github.singularity.core.syncservice.events.SyncEvent
import com.github.singularity.core.syncservice.events.SyncEventBridge
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.jvm.JvmInline

interface PluginEventHandler

@JvmInline
value class PluginWrapper(
    val plugins: List<Plugin>,
) {
    fun handleEvent(event: SyncEvent) = plugins.forEach { it.handleEvent(event) }
}


class PluginEventHandlerImpl(
    syncEventBridge: SyncEventBridge,
    pluginWrapper: PluginWrapper,
) : PluginEventHandler {

    init {
        syncEventBridge.incomingSyncEvents
            .onEach(pluginWrapper::handleEvent)
            .launchIn(CoroutineScope(Dispatchers.IO))
    }

}
