package com.github.singularity.core.datasource.database

import com.github.singularity.core.shared.model.PluginSettings
import kotlinx.coroutines.flow.Flow

interface PluginSettingsDataSource {

    val pluginSettings: Flow<List<PluginSettings>>

	fun getPluginSettings(pluginName: String): Flow<PluginSettings?>

	suspend fun insert(vararg pluginSettings: PluginSettings)

	suspend fun update(vararg pluginSettings: PluginSettings)

	suspend fun delete(vararg pluginSettings: PluginSettings)

}
