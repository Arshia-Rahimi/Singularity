package com.github.singularity.core.syncservice.plugin.clipboard

import com.github.singularity.core.syncservice.plugin.ClipboardPluginEvent
import com.github.singularity.core.syncservice.plugin.Plugin
import com.github.singularity.core.syncservice.plugin.SyncEvent

const val CLIPBOARD_PLUGIN_NAME = "Clipboard"

class ClipboardPlugin(
	private val platformClipboardPlugin: PlatformClipboardPlugin,
) : Plugin {

	override val pluginName = CLIPBOARD_PLUGIN_NAME

	override fun handleEvent(event: SyncEvent) {
		if (event !is ClipboardPluginEvent) return
		platformClipboardPlugin.copy(event.content)
	}

}
