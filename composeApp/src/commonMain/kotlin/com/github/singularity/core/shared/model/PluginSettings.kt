package com.github.singularity.core.shared.model

import kotlinx.serialization.Serializable

@Serializable
data class PluginSettings(
	val name: String,
	val pluginDataMap: PluginDataMap = emptyMap(),
	val isEnabled: Boolean = true,
)

typealias PluginDataMap = Map<String, String>
