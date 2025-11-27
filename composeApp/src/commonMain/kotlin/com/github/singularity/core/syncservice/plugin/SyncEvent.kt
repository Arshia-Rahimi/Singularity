package com.github.singularity.core.syncservice.plugin

import com.github.singularity.core.syncservice.plugin.clipboard.CLIPBOARD_PLUGIN_NAME
import kotlinx.serialization.Serializable

interface SyncEvent {
	val pluginName: String
}

@Serializable
sealed class Test : SyncEvent {

	override val pluginName = "Test"

	@Serializable
	data class TestEvent(
		val c: Int,
	) : Test()

}

@Serializable
class ClipboardPluginEvent(
	val content: String,
) : SyncEvent {
	override val pluginName = CLIPBOARD_PLUGIN_NAME
}
