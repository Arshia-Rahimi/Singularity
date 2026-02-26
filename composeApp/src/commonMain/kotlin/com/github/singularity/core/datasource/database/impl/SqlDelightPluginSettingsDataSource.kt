package com.github.singularity.core.datasource.database.impl

import app.cash.sqldelight.coroutines.asFlow
import com.github.singularity.core.database.SingularityDatabase
import com.github.singularity.core.datasource.database.PluginSettingsDataSource
import com.github.singularity.core.datasource.database.PluginSettingsModel
import com.github.singularity.core.syncservice.plugin.PluginOptions
import kotlinx.coroutines.flow.map

class SqlDelightPluginSettingsDataSource(
    db: SingularityDatabase,
) : PluginSettingsDataSource {

    private val queries = db.pluginsQueries

    override val pluginSettingsModel = queries.index()
        .asFlow()
        .map { query ->
            query.executeAsList()
                .map {
                    PluginSettingsModel(
                        name = it.name,
                        options = it.options,
                    )
                }
        }

    override suspend fun insert(vararg pluginSettingsModel: PluginSettingsModel) {
        queries.transaction {
            pluginSettingsModel.forEach {
                queries.insert(
                    name = it.name,
                    options = it.options,
                )
            }
        }
    }

    override suspend fun update(vararg plugin: Pair<String, PluginOptions>) {
        queries.transaction {
            plugin.forEach {
                queries.update(
                    name = it.first,
                    options = it.second,
                )
            }
        }
    }

    override suspend fun delete(vararg pluginName: String) {
        queries.transaction {
            pluginName.forEach(queries::delete)
        }
    }

}
