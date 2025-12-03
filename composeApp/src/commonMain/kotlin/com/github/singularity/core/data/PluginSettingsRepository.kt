package com.github.singularity.core.data

import com.github.singularity.core.datasource.database.PluginDataMap
import com.github.singularity.core.datasource.database.PluginSettingsModel
import com.github.singularity.core.syncservice.plugin.Plugin
import kotlinx.coroutines.flow.Flow

interface PluginSettingsRepository {

	val pluginSettingsModel: Flow<List<PluginSettingsModel>>

	suspend fun isEnabled(plugin: Plugin): Boolean

	fun getPluginSettings(pluginName: String): Flow<PluginSettingsModel?>

	suspend fun insert(vararg pluginSettingsModel: PluginSettingsModel)

	suspend fun toggleIsEnabled(pluginName: String)

	suspend fun updatePluginData(pluginName: String, pluginData: PluginDataMap)

}
