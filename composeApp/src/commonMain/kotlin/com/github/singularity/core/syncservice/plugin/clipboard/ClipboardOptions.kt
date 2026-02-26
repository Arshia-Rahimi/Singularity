package com.github.singularity.core.syncservice.plugin.clipboard

import com.github.singularity.core.syncservice.plugin.PluginOptions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Clipboard")
data class ClipboardOptions(
    override val isEnabled: Boolean,
) : PluginOptions {
    override fun withEnabled(enabled: Boolean) = copy(isEnabled = enabled)
}
