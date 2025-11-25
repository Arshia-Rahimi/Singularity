package com.github.singularity.core.syncservice.plugin.clipboard

import com.github.singularity.core.syncservice.SyncEvent
import kotlinx.serialization.Serializable

const val CLIPBOARD_PLUGIN_NAME = "Clipboard"

@Serializable
sealed class ClipboardPluginEvent : SyncEvent {

    override val pluginName = ClipboardPlugin::class.simpleName!!

    @Serializable
    data class Copied(
        val clipboardContent: String,
    ) : ClipboardPluginEvent()

}
