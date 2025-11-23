package com.github.singularity.core.datasource.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import com.github.singularity.core.database.SingularityDatabase
import com.github.singularity.core.datasource.PluginSettingsDataSource
import com.github.singularity.core.datasource.toBoolean
import com.github.singularity.core.datasource.toLong
import com.github.singularity.core.shared.model.PluginSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class SqlDelightPluginSettingsDataSource(
    db: SingularityDatabase,
) : PluginSettingsDataSource {

	private val queries = db.pluginSettingsQueries

	override val pluginSettings = queries.index()
		.asFlow()
		.map { query ->
			query.executeAsList().map {
				PluginSettings(
					name = it.name,
					isEnabled = it.is_enabled.toBoolean(),
					pluginData = Json.decodeFromString(it.plugin_data),
				)
			}
		}

	override fun getPluginSettings(pluginName: String) = queries.getPluginSettings(pluginName)
		.asFlow()
		.mapToOne(Dispatchers.IO)
		.map {
			PluginSettings(
				name = it.name,
				isEnabled = it.is_enabled.toBoolean(),
				pluginData = Json.decodeFromString(it.plugin_data),
			)
		}

	override suspend fun insert(pluginSettings: PluginSettings) {
		queries.insert(
			name = pluginSettings.name,
			is_enabled = pluginSettings.isEnabled.toLong(),
			plugin_data = Json.encodeToString(pluginSettings),
		)
    }

	override suspend fun update(pluginSettings: PluginSettings) {
		queries.update(
			name = pluginSettings.name,
			is_enabled = pluginSettings.isEnabled.toLong(),
			plugin_data = Json.encodeToString(pluginSettings.pluginData)
		)
    }

}
