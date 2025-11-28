package com.github.singularity.core.syncservice.plugin.clipboard

import com.github.singularity.core.datasource.memory.SyncEventBridge
import com.github.singularity.core.syncservice.plugin.ClipboardPluginEvent
import com.github.singularity.core.syncservice.plugin.Plugin
import com.github.singularity.core.syncservice.plugin.SyncEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

const val CLIPBOARD_PLUGIN_NAME = "Clipboard"

class ClipboardPlugin(
	private val platformClipboardPlugin: PlatformClipboardPlugin,
	syncEventBridge: SyncEventBridge,
) : Plugin {

	override val pluginName = CLIPBOARD_PLUGIN_NAME

	private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

	init {
		platformClipboardPlugin.systemClipboardUpdatedEvent
			.onEach {
				syncEventBridge.send(ClipboardPluginEvent(it))
			}.launchIn(scope)
	}

	override fun handleEvent(event: SyncEvent) {
		if (event !is ClipboardPluginEvent) return
		platformClipboardPlugin.copy(event.content)
	}

}
