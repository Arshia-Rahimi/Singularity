package com.github.singularity.core.syncservice.plugin.clipboard

import com.github.singularity.core.data.PluginSettingsRepository
import com.github.singularity.core.datasource.memory.SyncEventBridge
import com.github.singularity.core.syncservice.SyncEvent
import com.github.singularity.core.syncservice.plugin.Plugin
import com.github.singularity.core.syncservice.plugin.PluginSettingsManager
import com.github.singularity.core.syncservice.plugin.PluginSettingsManagerImpl

class ClipboardPlugin(
	private val syncEventBridge: SyncEventBridge,
	pluginSettingsRepository: PluginSettingsRepository,
) : Plugin,
	PluginSettingsManager by PluginSettingsManagerImpl(
		CLIPBOARD_PLUGIN_NAME,
		pluginSettingsRepository
	) {

	override val pluginName = CLIPBOARD_PLUGIN_NAME

	override fun handleEvent(syncEvent: SyncEvent) {
		if (syncEvent !is ClipboardPluginEvent) return
		if (!settings.value.isEnabled) return

		when (syncEvent) {
			is ClipboardPluginEvent.Copied -> copied(syncEvent.clipboardContent)
		}
	}

	private fun copied(clipboardContent: String) {

	}

}
