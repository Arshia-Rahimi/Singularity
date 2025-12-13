package com.github.singularity.ui.feature.plugins

import com.github.singularity.core.datasource.database.PluginSettingsModel

fun PluginSettingsModel.toPlugin() = Plugin(
	name = name,
	isEnabled = isEnabled,
)

data class Plugin(
	val name: String,
	val isEnabled: Boolean,
)

data class PluginsUiState(
    val plugins: List<Plugin> = emptyList(),
)
