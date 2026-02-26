package com.github.singularity.core.data.impl

import com.github.singularity.core.data.PluginSettingsRepository
import com.github.singularity.core.datasource.database.PluginSettingsDataSource
import com.github.singularity.core.datasource.database.PluginSettingsModel
import com.github.singularity.core.shared.util.shareInEagerly
import com.github.singularity.core.syncservice.plugin.PluginOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first

class SqlitePluginSettingsRepository(
    private val pluginSettingsDataSource: PluginSettingsDataSource,
) : PluginSettingsRepository {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override val pluginSettingsModel = pluginSettingsDataSource.pluginSettingsModel
        .shareInEagerly(scope, 1)

    override suspend fun insert(vararg pluginSettings: PluginSettingsModel) {
        pluginSettingsDataSource.insert(*pluginSettings)
    }

    override suspend fun update(name: String, options: PluginOptions) {
        pluginSettingsDataSource.update(name to options)
    }

    override suspend fun toggleIsEnabled(pluginName: String) {
        pluginSettingsModel.first()
            .firstOrNull { it.name == pluginName }
            ?.let {
                pluginSettingsDataSource.update(
                    pluginName to it.options.withEnabled(!it.options.isEnabled)
                )
            }
    }

}
