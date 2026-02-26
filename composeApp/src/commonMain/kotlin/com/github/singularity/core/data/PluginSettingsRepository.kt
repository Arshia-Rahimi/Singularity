package com.github.singularity.core.data

import com.github.singularity.core.datasource.database.PluginSettingsModel
import com.github.singularity.core.syncservice.plugin.PluginOptions
import kotlinx.coroutines.flow.Flow

interface PluginSettingsRepository {

	val pluginSettingsModel: Flow<List<PluginSettingsModel>>

	suspend fun insert(vararg pluginSettings: PluginSettingsModel)

	suspend fun update(name: String, options: PluginOptions)

	suspend fun toggleIsEnabled(pluginName: String)

}
