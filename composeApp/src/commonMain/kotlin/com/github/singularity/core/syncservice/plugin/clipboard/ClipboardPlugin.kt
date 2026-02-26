package com.github.singularity.core.syncservice.plugin.clipboard

import com.github.singularity.core.syncservice.SyncEventBridge
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

    companion object {
        const val PLUGIN_NAME = "Clipboard"
    }

    override val eventClass = ClipboardEvent::class

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    init {
        platformClipboardPlugin.systemClipboardUpdatedEvent.onEach {
            syncEventBridge.send(ClipboardEvent.Copied(it))
        }.launchIn(scope)
    }

    override fun handleEvent(event: SyncEvent) {
        if (!event.instanceOf(eventClass)) return
        when (event) {
            is ClipboardEvent.Copied -> platformClipboardPlugin.copy(event.content)
            is ClipboardEvent.SendToClipboard -> platformClipboardPlugin.copy(event.content)
        }
    }

}
