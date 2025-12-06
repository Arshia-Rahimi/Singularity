package com.github.singularity.core.data

import com.github.singularity.core.datasource.database.PluginSettingsData
import com.github.singularity.core.datasource.database.PluginSettingsModel
import com.github.singularity.core.syncservice.plugin.Plugin
import kotlinx.coroutines.flow.Flow

interface PluginSettingsRepository {

	val pluginSettingsModel: Flow<List<PluginSettingsModel>>

	suspend fun isEnabled(plugin: Plugin): Boolean

	fun getPluginSettings(pluginName: String): Flow<PluginSettingsModel?>

    fun getPluginSettingsData(pluginName: String, data: Pair<String, String?>): Flow<String?>

	suspend fun insert(vararg pluginSettingsModel: PluginSettingsModel)

	suspend fun toggleIsEnabled(pluginName: String)

    suspend fun updatePluginData(pluginName: String, pluginData: PluginSettingsData)

}
