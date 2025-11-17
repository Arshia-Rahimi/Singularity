package com.github.singularity.core.sync.plugin.clipboard

import com.github.singularity.core.datasource.memory.SyncEventBridge
import com.github.singularity.core.sync.SyncEventData
import com.github.singularity.core.sync.plugin.Plugin
import kotlinx.serialization.Serializable

private const val PLUGIN_NAME = "Clipboard"

class ClipboardPlugin(
    private val syncEventBridge: SyncEventBridge,
) : Plugin {

    @Serializable
    sealed class Events : SyncEventData {
        data class Copied(val clipboardContent: String) : Events()
    }

    override val pluginName: String = PLUGIN_NAME

    override fun handleEvent(event: SyncEventData) {
        when (event) {
            is Events.Copied -> copied(event.clipboardContent)
        }
    }

    private fun copied(clipboardContent: String) {

    }

//    private fun sendCopiedContent(clipboardContent: String) {
//        syncEventBridge.send(SyncEvent(PLUGIN_NAME, Events.Copied(clipboardContent)))
//    }

}
