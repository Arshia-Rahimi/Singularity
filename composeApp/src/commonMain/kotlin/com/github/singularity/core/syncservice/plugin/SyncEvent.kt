package com.github.singularity.core.syncservice.plugin

import com.github.singularity.core.syncservice.plugin.clipboard.CLIPBOARD_PLUGIN_NAME
import kotlinx.serialization.Serializable

interface SyncEvent {
	val pluginName: String
}

@Serializable
class ClipboardPluginEvent(
	val content: String,
) : SyncEvent {
	override val pluginName = CLIPBOARD_PLUGIN_NAME
}
