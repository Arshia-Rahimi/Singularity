package com.github.singularity.core.datasource.database.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.github.singularity.core.database.SingularityDatabase
import com.github.singularity.core.datasource.database.PluginSettingsDataSource
import com.github.singularity.core.shared.model.PluginSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class SqlDelightPluginSettingsDataSource(
	db: SingularityDatabase,
) : PluginSettingsDataSource {

	private val queries = db.pluginsQueries

	override val pluginSettings = queries.index()
		.asFlow()
		.map { query ->
			query.executeAsList()
				.map {
					PluginSettings(
						name = it.name,
						isEnabled = it.is_enabled.toBoolean(),
						pluginData = Json.decodeFromString(it.plugin_data),
					)
				}
		}

	override fun getPluginSettings(pluginName: String) =
		queries.getPlugin(pluginName)
			.asFlow()
			.mapToOneOrNull(Dispatchers.IO)
			.map {
				it?.let {
					PluginSettings(
						name = it.name,
						isEnabled = it.is_enabled.toBoolean(),
						pluginData = Json.decodeFromString(it.plugin_data),
					)
				}
			}

	override suspend fun insert(vararg pluginSettings: PluginSettings) {
		queries.transaction {
			pluginSettings.forEach {
				queries.insert(
					name = it.name,
					is_enabled = it.isEnabled.toLong(),
					plugin_data = Json.encodeToString(it.pluginData),
				)
			}
		}
	}

	override suspend fun update(vararg pluginSettings: PluginSettings) {
		queries.transaction {
			pluginSettings.forEach {
				queries.update(
					is_enabled = it.isEnabled.toLong(),
					name = it.name,
					plugin_data = Json.encodeToString(it.pluginData),
				)
			}
		}
	}

	override suspend fun delete(vararg pluginSettings: PluginSettings) {
		queries.transaction {
			pluginSettings.forEach {
				queries.delete(name = it.name)
			}
		}
	}

}
