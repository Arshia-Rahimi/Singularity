package com.github.singularity.core.datasource.database

import kotlinx.coroutines.flow.Flow

interface PluginSettingsDataSource {

	val pluginSettingsModel: Flow<List<PluginSettingsModel>>

	fun getPluginSettings(pluginName: String): Flow<PluginSettingsModel?>

	suspend fun insert(vararg pluginSettingsModel: PluginSettingsModel)

	suspend fun update(vararg pluginSettingsModel: PluginSettingsModel)

	suspend fun delete(vararg pluginSettingsModel: PluginSettingsModel)

}
