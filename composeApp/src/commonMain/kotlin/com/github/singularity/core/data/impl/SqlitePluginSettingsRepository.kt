package com.github.singularity.core.data.impl

import com.github.singularity.core.data.PluginSettingsRepository
import com.github.singularity.core.datasource.database.PluginDataMap
import com.github.singularity.core.datasource.database.PluginSettingsDataSource
import com.github.singularity.core.datasource.database.PluginSettingsModel
import com.github.singularity.core.shared.util.onFirst
import com.github.singularity.core.shared.util.shareInEagerly
import com.github.singularity.core.syncservice.plugin.Plugin
import com.github.singularity.core.syncservice.plugin.PluginWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first

class SqlitePluginSettingsRepository(
	private val pluginSettingsDataSource: PluginSettingsDataSource,
	pluginWrapper: PluginWrapper,
) : PluginSettingsRepository {

	private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

	override val pluginSettingsModel = pluginSettingsDataSource.pluginSettingsModel
		.onFirst { pluginSettings ->
			val plugins = pluginSettings.map { it.name }
			val unsavedPlugins =
				pluginWrapper.plugins.mapNotNull { it::class.simpleName }.filter { it !in plugins }
			insert(*unsavedPlugins.map { PluginSettingsModel(it) }.toTypedArray())
		}
		.shareInEagerly(scope, 1)

	override suspend fun isEnabled(plugin: Plugin) = pluginSettingsModel.first()
		.firstOrNull { it.name == plugin::class.simpleName!! }?.isEnabled ?: false

	override fun getPluginSettings(pluginName: String) =
		pluginSettingsDataSource.getPluginSettings(pluginName)

	override suspend fun insert(vararg pluginSettingsModel: PluginSettingsModel) {
		pluginSettingsDataSource.insert(*pluginSettingsModel)
	}

	override suspend fun toggleIsEnabled(pluginName: String) {
		pluginSettingsModel.first().firstOrNull { it.name == pluginName }
			?.let { pluginSettingsDataSource.update(it.copy(isEnabled = !it.isEnabled)) }
	}

	override suspend fun updatePluginData(
		pluginName: String,
		pluginData: PluginDataMap,
	) {
		pluginSettingsModel.first().firstOrNull { it.name == pluginName }
			?.let { pluginSettingsDataSource.update(it.copy(pluginDataMap = pluginData)) }
	}

}
