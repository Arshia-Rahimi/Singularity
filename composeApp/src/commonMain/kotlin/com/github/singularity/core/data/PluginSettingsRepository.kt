package com.github.singularity.core.data

import com.github.singularity.core.shared.model.PluginDataMap
import com.github.singularity.core.shared.model.PluginSettings
import com.github.singularity.core.syncservice.plugin.Plugin
import kotlinx.coroutines.flow.Flow

interface PluginSettingsRepository {

	val pluginSettings: Flow<List<PluginSettings>>

	suspend fun isEnabled(plugin: Plugin): Boolean

	fun getPluginSettings(pluginName: String): Flow<PluginSettings?>

	suspend fun insert(vararg pluginSettings: PluginSettings)

	suspend fun toggleIsEnabled(pluginName: String)

	suspend fun updatePluginData(pluginName: String, pluginData: PluginDataMap)

}
