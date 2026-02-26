package com.github.singularity.core.datasource.database

import com.github.singularity.core.syncservice.plugin.PluginOptions
import kotlinx.coroutines.flow.Flow

interface PluginSettingsDataSource {

	val pluginSettingsModel: Flow<List<PluginSettingsModel>>

	suspend fun insert(vararg pluginSettingsModel: PluginSettingsModel)

	suspend fun update(vararg plugin: Pair<String, PluginOptions>)

	suspend fun delete(vararg pluginName: String)

}
