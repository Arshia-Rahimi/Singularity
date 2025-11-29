package com.github.singularity.core.syncservice.plugin.clipboard

import com.github.singularity.core.datasource.memory.SyncEventBridge
import com.github.singularity.core.syncservice.plugin.ClipboardPluginEvent
import com.github.singularity.core.syncservice.plugin.Plugin
import com.github.singularity.core.syncservice.plugin.SyncEvent
import io.ktor.util.reflect.instanceOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ClipboardPlugin(
	private val platformClipboardPlugin: PlatformClipboardPlugin,
	syncEventBridge: SyncEventBridge,
) : Plugin {

	override val eventClass = ClipboardPluginEvent::class

	private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

	init {
		platformClipboardPlugin.systemClipboardUpdatedEvent
			.onEach {
				syncEventBridge.send(ClipboardPluginEvent.Copied(it))
			}.launchIn(scope)
	}

	override fun handleEvent(event: SyncEvent) {
		println(event)
		if (!event.instanceOf(eventClass)) return
		when (event) {
			is ClipboardPluginEvent.Copied -> platformClipboardPlugin.copy(event.content)
		}
	}

}
