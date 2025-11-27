package com.github.singularity.core.syncservice.plugin

import com.github.singularity.core.data.PluginSettingsRepository
import com.github.singularity.core.datasource.memory.SyncEventBridge
import com.github.singularity.core.shared.model.PluginSettings
import com.github.singularity.core.shared.util.onFirst
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

interface PluginEventHandler

class PluginEventHandlerImpl(
	pluginSettingsRepo: PluginSettingsRepository,
	syncEventBridge: SyncEventBridge,
	pluginWrapper: PluginWrapper,
) : PluginEventHandler {

	private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

	private val settings = pluginSettingsRepo.pluginSettings
		.onFirst { savedPlugins ->
			val plugins = pluginWrapper.plugins.map { it.pluginName }
			val newPlugins = plugins.filter { plugin -> plugin !in savedPlugins.map { it.name } }

			pluginSettingsRepo.insert(*newPlugins.map { PluginSettings(it) }.toTypedArray())
		}.stateIn(
			scope,
			SharingStarted.Eagerly,
			emptyList(),
		)

	init {
		syncEventBridge.incomingSyncEvents.onEach { event ->
			val plugin = pluginWrapper.plugins.firstOrNull { plugin ->
				plugin.pluginName == event.pluginName
			} ?: return@onEach

			if (settings.first().first { it.name == plugin.pluginName }.isEnabled) {
				plugin.handleEvent(event)
			}

		}.launchIn(scope)
	}

}
