package com.github.singularity.core.datasource.database

import kotlinx.coroutines.flow.Flow

interface PluginSettingsDataSource {

	val pluginSettings: Flow<List<PluginSettingsModel>>

	suspend fun insert(vararg pluginSettings: PluginSettingsModel)

	suspend fun update(vararg pluginSettings: PluginSettingsModel)

}
