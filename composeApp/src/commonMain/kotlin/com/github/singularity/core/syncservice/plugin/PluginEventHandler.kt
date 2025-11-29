package com.github.singularity.core.syncservice.plugin

import com.github.singularity.core.data.PluginSettingsRepository
import com.github.singularity.core.datasource.memory.SyncEventBridge
import io.ktor.util.reflect.instanceOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

interface PluginEventHandler

class PluginEventHandlerImpl(
	private val pluginSettingsRepo: PluginSettingsRepository,
	syncEventBridge: SyncEventBridge,
	pluginWrapper: PluginWrapper,
) : PluginEventHandler {

	private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

	init {
		syncEventBridge.incomingSyncEvents.onEach { event ->
			val plugin = pluginWrapper.plugins.firstOrNull { plugin ->
				event.instanceOf(plugin.eventClass)
			} ?: return@onEach

			if (pluginSettingsRepo.isEnabled(plugin)) {
				plugin.handleEvent(event)
			}

		}.launchIn(scope)
	}

}
