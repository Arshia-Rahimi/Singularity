package com.github.singularity.core.syncservice.plugin.clipboard

import com.github.singularity.core.shared.model.PluginData
import com.github.singularity.core.syncservice.SyncEvent
import kotlinx.serialization.Serializable

@Serializable
sealed class ClipboardPluginEvent : SyncEvent {

    override val pluginName = ClipboardPlugin::class.simpleName!!

    @Serializable
    data class Copied(
        val clipboardContent: String,
    ) : ClipboardPluginEvent()

}

@Serializable
data class ClipboardPluginSettingsData(
	val a: String = "a",
) : PluginData
