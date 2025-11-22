package com.github.singularity.core.syncservice.plugin.clipboard

import com.github.singularity.core.datasource.SyncEventBridge
import com.github.singularity.core.syncservice.plugin.Plugin
import com.github.singularity.core.syncservice.plugin.SyncEvent

const val CLIPBOARD_PLUGIN_NAME = "Clipboard"

class ClipboardPlugin(
	private val syncEventBridge: SyncEventBridge,
) : Plugin {

	override val pluginName: String = CLIPBOARD_PLUGIN_NAME

	override fun handleEvent(syncEvent: SyncEvent) {
		if (syncEvent !is SyncEvent.Clipboard) return

		when (syncEvent) {
			is SyncEvent.Clipboard.Copied -> copied(syncEvent.clipboardContent)
		}
	}

	private fun copied(clipboardContent: String) {

	}

}
