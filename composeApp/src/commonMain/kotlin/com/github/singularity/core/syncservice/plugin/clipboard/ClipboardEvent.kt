package com.github.singularity.core.syncservice.plugin.clipboard

import com.github.singularity.core.shared.model.PluginData
import com.github.singularity.core.syncservice.SyncEvent
import kotlinx.serialization.Serializable

@Serializable
sealed class ClipboardEvent : SyncEvent {

    override val pluginName = ClipboardPlugin::class.simpleName!!

    @Serializable
    data class Copied(
        val clipboardContent: String,
    ) : ClipboardEvent()

}

data class ClipboardPluginSettingsData(
	val a: String = "a",
) : PluginData
