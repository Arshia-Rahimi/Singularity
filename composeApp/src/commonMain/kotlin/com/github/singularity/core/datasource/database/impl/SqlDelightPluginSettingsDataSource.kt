package com.github.singularity.core.datasource.database.impl

import app.cash.sqldelight.coroutines.asFlow
import com.github.singularity.core.database.SingularityDatabase
import com.github.singularity.core.datasource.database.PluginSettingsDataSource
import com.github.singularity.core.datasource.database.PluginSettingsModel
import kotlinx.coroutines.flow.map

class SqlDelightPluginSettingsDataSource(
    db: SingularityDatabase,
) : PluginSettingsDataSource {

    private val queries = db.pluginsQueries

    override val pluginSettings = queries.index()
        .asFlow()
        .map { query ->
            query.executeAsList().map { PluginSettingsModel(it.name, it.options) }
        }

    override suspend fun insert(vararg pluginSettings: PluginSettingsModel) {
        queries.transaction {
            pluginSettings.forEach { queries.insert(it.name, it.options) }
        }
    }

    override suspend fun update(vararg pluginSettings: PluginSettingsModel) {
        queries.transaction {
            pluginSettings.forEach { queries.update(it.options, it.name) }
        }
    }

}
