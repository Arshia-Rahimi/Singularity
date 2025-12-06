package com.github.singularity.core.syncservice.plugin.clipboard

import com.github.singularity.core.datasource.database.PluginSettingsData
import com.github.singularity.core.syncservice.SyncEventBridge
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

expect val platformClipboardPluginSettingsData: PluginSettingsData

class ClipboardPlugin(
    private val platformClipboardPlugin: PlatformClipboardPlugin,
    syncEventBridge: SyncEventBridge,
) : Plugin {

    override val pluginName = PLUGIN_NAME

    companion object {
        const val PLUGIN_NAME = "Clipboard"

        val pluginSettingsData: PluginSettingsData =
            emptyMap<String, String?>() + platformClipboardPluginSettingsData
    }

    override val eventClass = ClipboardPluginEvent::class

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    init {
        platformClipboardPlugin.systemClipboardUpdatedEvent.onEach {
            syncEventBridge.send(ClipboardPluginEvent.Copied(it))
        }.launchIn(scope)
    }

    override fun handleEvent(event: SyncEvent) {
        if (!event.instanceOf(eventClass)) return
        when (event) {
            is ClipboardPluginEvent.Copied -> platformClipboardPlugin.copy(event.content)
            is ClipboardPluginEvent.SendToClipboard -> platformClipboardPlugin.copy(event.content)
        }
    }

}
