package com.github.singularity.core.syncservice.plugin

import com.github.singularity.core.datasource.memory.SyncEventBridge
import com.github.singularity.core.syncservice.PluginWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

interface PluginManager

class PluginManagerImpl(
	private val pluginWrapper: PluginWrapper,
	syncEventBridge: SyncEventBridge,
) : PluginManager {

	private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

	init {
		syncEventBridge.incomingSyncEvents.onEach { event ->
			pluginWrapper.plugins.filter { it.settings.value.isEnabled }
				.firstOrNull { plugin ->
					plugin::class.simpleName == event.pluginName
				}?.handleEvent(event)
		}.launchIn(scope)
	}

}
