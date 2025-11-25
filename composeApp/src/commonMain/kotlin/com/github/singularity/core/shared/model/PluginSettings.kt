package com.github.singularity.core.shared.model

import kotlinx.serialization.Serializable

@Serializable
data class PluginSettings(
    val name: String,
    val pluginData: PluginData,
    val isEnabled: Boolean = true,
)

interface PluginData
