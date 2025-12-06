package com.github.singularity.core.data.impl

import com.github.singularity.core.data.PluginSettingsRepository
import com.github.singularity.core.datasource.database.PluginSettingsData
import com.github.singularity.core.datasource.database.PluginSettingsDataSource
import com.github.singularity.core.datasource.database.PluginSettingsModel
import com.github.singularity.core.shared.util.shareInEagerly
import com.github.singularity.core.syncservice.plugin.Plugin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SqlitePluginSettingsRepository(
    private val pluginSettingsDataSource: PluginSettingsDataSource,
) : PluginSettingsRepository {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override val pluginSettingsModel = pluginSettingsDataSource.pluginSettingsModel
        .shareInEagerly(scope, 1)

    override suspend fun isEnabled(plugin: Plugin) = pluginSettingsModel.first()
        .firstOrNull { it.name == plugin::class.simpleName!! }?.isEnabled ?: false

    override fun getPluginSettings(pluginName: String) =
        pluginSettingsDataSource.getPluginSettings(pluginName)

    override fun getPluginSettingsData(
        pluginName: String,
        data: Pair<String, String?>,
    ): Flow<String?> =
        getPluginSettings(pluginName).map { it?.data[data.first] ?: data.second }


    override suspend fun insert(vararg pluginSettingsModel: PluginSettingsModel) {
        pluginSettingsDataSource.insert(*pluginSettingsModel)
    }

    override suspend fun toggleIsEnabled(pluginName: String) {
        pluginSettingsModel.first().firstOrNull { it.name == pluginName }
            ?.let { pluginSettingsDataSource.update(it.copy(isEnabled = !it.isEnabled)) }
    }

    override suspend fun updatePluginData(
        pluginName: String,
        pluginData: PluginSettingsData,
    ) {
        pluginSettingsModel.first().firstOrNull { it.name == pluginName }
            ?.let { pluginSettingsDataSource.update(it.copy(data = pluginData)) }
    }

}
