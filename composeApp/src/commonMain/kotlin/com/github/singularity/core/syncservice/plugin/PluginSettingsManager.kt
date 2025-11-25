package com.github.singularity.core.syncservice.plugin

import com.github.singularity.core.data.PluginSettingsRepository
import com.github.singularity.core.shared.model.PluginSettings
import com.github.singularity.core.shared.util.stateInWhileSubscribed
import com.github.singularity.core.syncservice.plugin.clipboard.ClipboardPluginSettingsData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.StateFlow

interface PluginSettingsManager {

	val settings: StateFlow<PluginSettings>

}

class PluginSettingsManagerImpl(
	private val pluginSettingsRepo: PluginSettingsRepository,
) : PluginSettingsManager {

	private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

	override val settings = pluginSettingsRepo.getPluginSettings(this::class.simpleName!!)
		.stateInWhileSubscribed(
			PluginSettings(
				this::class.simpleName!!,
				ClipboardPluginSettingsData()
			), scope
		)

}
