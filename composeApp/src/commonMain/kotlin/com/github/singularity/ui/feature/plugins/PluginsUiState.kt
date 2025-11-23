package com.github.singularity.ui.feature.plugins

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.github.singularity.core.shared.model.PluginSettings

data class PluginsUiState(
	val plugins: SnapshotStateList<PluginSettings> = mutableStateListOf(),
)
