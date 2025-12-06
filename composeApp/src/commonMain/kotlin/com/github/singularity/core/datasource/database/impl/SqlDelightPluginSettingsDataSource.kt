package com.github.singularity.core.datasource.database.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.github.singularity.core.database.SingularityDatabase
import com.github.singularity.core.datasource.database.PluginSettingsData
import com.github.singularity.core.datasource.database.PluginSettingsDataSource
import com.github.singularity.core.datasource.database.PluginSettingsModel
import com.github.singularity.core.shared.util.filterNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.map

class SqlDelightPluginSettingsDataSource(
	db: SingularityDatabase,
) : PluginSettingsDataSource {

	private val queries = db.pluginsQueries

	override val pluginSettingsModel = queries.index()
		.asFlow()
		.map { query ->
			query.executeAsList()
				.groupBy { it.name }
				.map { (pluginName, data) ->
                    val pluginData: PluginSettingsData = data
						.associate { it.data_key to it.data_value }
						.filterNotNull()

					PluginSettingsModel(
						name = pluginName,
						isEnabled = data.first().is_enabled.toBoolean(),
                        data = pluginData,
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
				.filterNotNull()

			PluginSettingsModel(
				name = queryResult.first().name,
				isEnabled = queryResult.first().is_enabled.toBoolean(),
                data = pluginData,
			)
		}

	override suspend fun insert(vararg pluginSettingsModel: PluginSettingsModel) {
		queries.transaction {
			pluginSettingsModel.forEach {
				queries.insert(
					name = it.name,
					is_enabled = it.isEnabled.toLong(),
				)
                it.data.toMap().forEach { (key, value) ->
					queries.insertPluginData(
						plugin_name = it.name,
						data_key = key,
						data_value = value,
					)
				}
			}
		}
	}

	override suspend fun update(vararg pluginSettingsModel: PluginSettingsModel) {
		queries.transaction {
			pluginSettingsModel.forEach {
				queries.update(
					is_enabled = it.isEnabled.toLong(),
					name = it.name,
				)
                it.data.toMap().forEach { (key, value) ->
					queries.updatePluginData(
						data_value = value,
						data_key = key,
						plugin_name = it.name,
					)
				}
			}
		}
	}

	override suspend fun delete(vararg pluginSettingsModel: PluginSettingsModel) {
		queries.transaction {
			pluginSettingsModel.forEach {
				queries.delete(name = it.name)
                it.data.toMap().forEach { (key, _) ->
					queries.deletePluginData(
						data_key = key,
						plugin_name = it.name,
					)
				}
			}
		}
	}

}
