package com.github.singularity.core.data.impl

import com.github.singularity.core.data.PluginSettingsRepository
import com.github.singularity.core.datasource.database.PluginSettingsDataSource
import com.github.singularity.core.datasource.database.PluginSettingsModel
import com.github.singularity.core.shared.PluginOption
import com.github.singularity.core.shared.toPluginOptions
import com.github.singularity.core.shared.util.onFirst
import com.github.singularity.core.shared.util.shareInEagerly
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first

class SqlitePluginSettingsRepository(
    private val pluginSettingsDataSource: PluginSettingsDataSource,
) : PluginSettingsRepository {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override val pluginSettings = pluginSettingsDataSource.pluginSettings
        .onFirst(::insertPlugins)
        .shareInEagerly(scope, 1)

    override suspend fun insert(vararg pluginSettings: PluginSettingsModel) {
        pluginSettingsDataSource.insert(*pluginSettings)
    }

    override suspend fun toggleIsEnabled(pluginName: String) {
        // todo
        update(pluginName)
    }

    private suspend fun update(name: String, vararg newOption: PluginOption) {
        val settings = pluginSettings.first().firstOrNull { it.name == name } ?: return
        val oldOptions = settings.options.options

        val newOptions = buildList {
            addAll(oldOptions)

            newOption.forEach { newOption ->
                if (newOption in oldOptions) {
                    removeAll { it.name == newOption.name }
                }
                add(newOption)
            }
        }.toPluginOptions()

        pluginSettingsDataSource.update(
            PluginSettingsModel(name, newOptions)
        )
    }

    private suspend fun insertPlugins(pluginSettings: List<PluginSettingsModel>) {
        // todo
    }

}
