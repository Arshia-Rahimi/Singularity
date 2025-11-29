package com.github.singularity.core.syncservice.plugin

import kotlinx.serialization.Serializable

@Serializable
sealed interface SyncEvent

@Serializable
sealed class ClipboardPluginEvent : SyncEvent {

	@Serializable
	data class Copied(val content: String) : ClipboardPluginEvent()

}
