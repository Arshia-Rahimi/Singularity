package com.github.singularity.core.datasource

import com.github.singularity.core.shared.model.PluginData
import com.github.singularity.core.shared.model.PluginSettings
import kotlinx.coroutines.flow.Flow

interface PluginSettingsDataSource {

    val pluginSettings: Flow<List<PluginSettings>>

    fun getPluginSettings(pluginName: String): Flow<PluginSettings>

    fun insert(pluginSettings: PluginSettings)

    fun updatePluginData(pluginName: String, pluginData: PluginData)

    fun updatePluginIsEnabled(pluginName: String, isEnabled: Boolean)

}
