package com.github.singularity.core.datasource.database.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.github.singularity.core.database.SingularityDatabase
import com.github.singularity.core.datasource.database.PluginSettingsDataSource
import com.github.singularity.core.shared.model.PluginData
import com.github.singularity.core.shared.model.PluginSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.map

@Suppress("UNCHECKED_CAST")
class SqlDelightPluginSettingsDataSource(
	db: SingularityDatabase,
) : PluginSettingsDataSource {

	private val queries = db.pluginsQueries

	override val pluginSettings = queries.index()
		.asFlow()
		.map { query ->
			query.executeAsList()
				.groupBy { it.name }
				.map { (pluginName, data) ->
					val pluginData = data
						.associate { it.data_key to it.data_value }
						.filterKeys { it != null }
						.filterValues { it != null } as PluginData

					PluginSettings(
						name = pluginName,
						isEnabled = data.first().is_enabled.toBoolean(),
						pluginData = pluginData,
					)
				}
		}

	override fun getPluginSettings(pluginName: String) = queries.getPlugin(pluginName)
		.asFlow()
		.mapToList(Dispatchers.IO)
		.map { queryResult ->
			if (queryResult.isEmpty()) return@map null

			val pluginData = queryResult
				.associate { it.data_key to it.data_value }
				.filterKeys { it != null }
				.filterValues { it != null } as PluginData

			PluginSettings(
				name = queryResult.first().name,
				isEnabled = queryResult.first().is_enabled.toBoolean(),
				pluginData = pluginData,
			)
		}

	override suspend fun insert(vararg pluginSettings: PluginSettings) {
		queries.transaction {
			pluginSettings.forEach {
				queries.insert(
					name = it.name,
					is_enabled = it.isEnabled.toLong(),
				)
				it.pluginData.forEach { (key, value) ->
					queries.insertPluginData(
						plugin_name = it.name,
						data_key = key,
						data_value = value,
					)
				}
			}
		}
	}

	override suspend fun update(vararg pluginSettings: PluginSettings) {
		queries.transaction {
			pluginSettings.forEach {
				queries.update(
					is_enabled = it.isEnabled.toLong(),
					name = it.name,
				)
				it.pluginData.forEach { (key, value) ->
					queries.updatePluginData(
						data_value = value,
						data_key = key,
						plugin_name = it.name,
					)
				}
			}
		}
	}

	override suspend fun delete(vararg pluginSettings: PluginSettings) {
		queries.transaction {
			pluginSettings.forEach {
				queries.delete(name = it.name)
				it.pluginData.forEach { (key, _) ->
					queries.deletePluginData(
						data_key = key,
						plugin_name = it.name,
					)
				}
			}
		}
	}

}
