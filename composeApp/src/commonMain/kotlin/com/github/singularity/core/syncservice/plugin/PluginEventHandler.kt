package com.github.singularity.core.syncservice.plugin

import com.github.singularity.core.datasource.memory.SyncEventBridge
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

interface PluginEventHandler

class PluginEventHandlerImpl(
	private val pluginWrapper: PluginWrapper,
	syncEventBridge: SyncEventBridge,
) : PluginEventHandler {

	private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

	init {
		syncEventBridge.incomingSyncEvents.onEach { event ->
			pluginWrapper.plugins.firstOrNull { plugin ->
				plugin::class.simpleName == event.pluginName
			}?.handleEvent(event)
		}.launchIn(scope)
	}

}
