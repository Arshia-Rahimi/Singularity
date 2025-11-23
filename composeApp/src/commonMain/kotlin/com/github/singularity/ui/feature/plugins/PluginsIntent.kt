package com.github.singularity.ui.feature.plugins

sealed interface PluginsIntent {
	data class ToggleIsEnabled(val pluginName: String) : PluginsIntent
}
