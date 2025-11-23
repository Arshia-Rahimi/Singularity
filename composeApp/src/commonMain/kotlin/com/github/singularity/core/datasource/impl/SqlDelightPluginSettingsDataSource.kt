package com.github.singularity.core.datasource.impl

import com.github.singularity.core.database.SingularityDatabase
import com.github.singularity.core.datasource.PluginSettingsDataSource
import com.github.singularity.core.shared.model.PluginData
import com.github.singularity.core.shared.model.PluginSettings
import kotlinx.coroutines.flow.Flow

class SqlDelightPluginSettingsDataSource(
    db: SingularityDatabase,
) : PluginSettingsDataSource {

    private val queries = db

    override val pluginSettings: Flow<List<PluginSettings>>
        get() = TODO("Not yet implemented")

    override fun getPluginSettings(pluginName: String): Flow<PluginSettings> {
        TODO("Not yet implemented")
    }

    override fun insert(pluginSettings: PluginSettings) {
        TODO("Not yet implemented")
    }

    override fun updatePluginData(
        pluginName: String,
        pluginData: PluginData
    ) {
        TODO("Not yet implemented")
    }

    override fun updatePluginIsEnabled(pluginName: String, isEnabled: Boolean) {
        TODO("Not yet implemented")
    }

}
