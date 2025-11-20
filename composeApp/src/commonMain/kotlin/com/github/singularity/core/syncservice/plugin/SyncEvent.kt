package com.github.singularity.core.syncservice.plugin

import com.github.singularity.core.syncservice.plugin.clipboard.CLIPBOARD_PLUGIN_NAME
import kotlinx.serialization.Serializable

@Serializable
sealed interface SyncEvent {
	val pluginName: String

	@Serializable
	sealed class Clipboard : SyncEvent {
		override val pluginName = CLIPBOARD_PLUGIN_NAME

		@Serializable
		data class Copied(
			val clipboardContent: String,
		) : Clipboard()

	}

	@Serializable
	sealed class Test : SyncEvent {
		override val pluginName = "test"

		@Serializable
		data class TestEvent(
			val c: Int,
		) : Test()
	}
}
