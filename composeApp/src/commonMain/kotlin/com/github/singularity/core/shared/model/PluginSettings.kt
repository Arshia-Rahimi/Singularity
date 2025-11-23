package com.github.singularity.core.shared.model

import kotlinx.serialization.Serializable

@Serializable
data class PluginSettings(
    val name: String,
    val isEnabled: Boolean,
    val pluginData: PluginData,
)

interface PluginData
