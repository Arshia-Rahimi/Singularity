package com.github.singularity.ui.feature.plugins

import com.github.singularity.core.datasource.database.PluginSettingsModel
import com.github.singularity.core.shared.PluginOptions

fun PluginSettingsModel.toPlugin() = Plugin(
    name = name,
    options = options,
)

data class Plugin(
    val name: String,
    val options: PluginOptions,
) {
    val isEnabled: Boolean
        get() = options.options.firstOrNull { it.name == "isEnabled" }?.getValue<Boolean>() ?: false
}

data class PluginsUiState(
    val plugins: List<Plugin> = emptyList(),
)
