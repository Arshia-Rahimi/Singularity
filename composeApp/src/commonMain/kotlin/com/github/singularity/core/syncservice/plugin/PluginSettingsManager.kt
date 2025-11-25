package com.github.singularity.core.syncservice.plugin

import com.github.singularity.core.data.PluginSettingsRepository
import com.github.singularity.core.shared.model.PluginSettings
import com.github.singularity.core.shared.util.onFirst
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn

interface PluginSettingsManager {

	val settings: StateFlow<PluginSettings>

}

class PluginSettingsManagerImpl(
	pluginName: String,
	private val pluginSettingsRepo: PluginSettingsRepository,
) : PluginSettingsManager {

	private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

	override val settings = pluginSettingsRepo.getPluginSettings(pluginName)
		.onFirst {
			if (it != null) return@onFirst
			pluginSettingsRepo.insert(PluginSettings(pluginName))
		}
		.filterNotNull()
		.stateIn(
			scope,
			SharingStarted.Eagerly,
			PluginSettings(pluginName),
		)

}
