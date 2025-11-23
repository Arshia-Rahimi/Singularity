package com.github.singularity.core.data.impl

import com.github.singularity.core.data.PluginSettingsRepository
import com.github.singularity.core.datasource.PluginSettingsDataSource
import com.github.singularity.core.shared.model.PluginData
import com.github.singularity.core.shared.util.shareInWhileSubscribed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first

class SqlitePluginSettingsRepository(
	private val pluginSettingsDataSource: PluginSettingsDataSource,
) : PluginSettingsRepository {

	private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

	override val pluginSettings = pluginSettingsDataSource.pluginSettings
		.shareInWhileSubscribed(scope)

	override fun getPluginSettings(pluginName: String) =
		pluginSettingsDataSource.getPluginSettings(pluginName)

	override suspend fun toggleIsEnabled(pluginName: String) {
		pluginSettings.first().firstOrNull { it.name == pluginName }
			?.let { pluginSettingsDataSource.update(it.copy(isEnabled = !it.isEnabled)) }
	}

	override suspend fun updatePluginData(
		pluginName: String,
		pluginData: PluginData
	) {
		pluginSettings.first().firstOrNull { it.name == pluginName }
			?.let { pluginSettingsDataSource.update(it.copy(pluginData = pluginData)) }
	}

}
