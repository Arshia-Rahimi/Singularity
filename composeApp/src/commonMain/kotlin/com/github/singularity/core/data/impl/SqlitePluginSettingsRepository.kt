package com.github.singularity.core.data.impl

import com.github.singularity.core.data.PluginSettingsRepository
import com.github.singularity.core.datasource.database.PluginSettingsDataSource
import com.github.singularity.core.shared.model.PluginDataMap
import com.github.singularity.core.shared.model.PluginSettings
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
		.shareInWhileSubscribed(scope, 1)

	override fun getPluginSettings(pluginName: String) =
		pluginSettingsDataSource.getPluginSettings(pluginName)

	override suspend fun insert(pluginSettings: PluginSettings) {
		pluginSettingsDataSource.insert(pluginSettings)
	}

	override suspend fun toggleIsEnabled(pluginName: String) {
		pluginSettings.first().firstOrNull { it.name == pluginName }
			?.let { pluginSettingsDataSource.update(it.copy(isEnabled = !it.isEnabled)) }
	}

	override suspend fun updatePluginData(
		pluginName: String,
		pluginData: PluginDataMap,
	) {
		pluginSettings.first().firstOrNull { it.name == pluginName }
			?.let { pluginSettingsDataSource.update(it.copy(pluginDataMap = pluginData)) }
	}

}
