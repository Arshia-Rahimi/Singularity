package com.github.singularity.core.syncservice.plugin.clipboard

import com.github.singularity.core.data.PluginSettingsRepository
import com.github.singularity.core.datasource.memory.SyncEventBridge
import com.github.singularity.core.shared.model.PluginSettings
import com.github.singularity.core.shared.util.stateInWhileSubscribed
import com.github.singularity.core.syncservice.SyncEvent
import com.github.singularity.core.syncservice.plugin.Plugin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob

class ClipboardPlugin(
	private val syncEventBridge: SyncEventBridge,
	pluginSettingsRepository: PluginSettingsRepository,
) : Plugin {

	private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

	override val settings = pluginSettingsRepository.getPluginSettings(this::class.simpleName!!)
		.stateInWhileSubscribed(
			PluginSettings(
				this::class.simpleName!!,
				ClipboardPluginSettingsData()
			), scope
		)

	override fun handleEvent(syncEvent: SyncEvent) {
		if (syncEvent !is ClipboardEvent) return
		when (syncEvent) {
			is ClipboardEvent.Copied -> copied(syncEvent.clipboardContent)
		}
	}

	private fun copied(clipboardContent: String) {

	}

}
