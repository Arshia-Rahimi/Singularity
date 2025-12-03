package com.github.singularity.ui.feature.plugins

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
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
	val plugins: SnapshotStateList<Plugin> = mutableStateListOf(),
)
