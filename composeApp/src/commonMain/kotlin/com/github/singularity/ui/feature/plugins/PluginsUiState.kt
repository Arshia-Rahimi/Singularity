package com.github.singularity.ui.feature.plugins

import com.github.singularity.core.datasource.database.PluginSettingsModel
import com.github.singularity.core.syncservice.plugin.PluginOptions

fun PluginSettingsModel.toPlugin() = Plugin(
	name = name,
	options = options,
)

data class Plugin(
	val name: String,
	val options: PluginOptions,
) {
	val isEnabled: Boolean
		get() = options.isEnabled
}

data class PluginsUiState(
    val plugins: List<Plugin> = emptyList(),
)
