package com.github.singularity.core.data

import com.github.singularity.core.datasource.database.PluginSettingsModel
import kotlinx.coroutines.flow.Flow

interface PluginSettingsRepository {

	val pluginSettings: Flow<List<PluginSettingsModel>>

	suspend fun insert(vararg pluginSettings: PluginSettingsModel)

	suspend fun toggleIsEnabled(pluginName: String)

}
