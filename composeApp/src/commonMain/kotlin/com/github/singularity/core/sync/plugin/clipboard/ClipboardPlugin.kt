package com.github.singularity.core.sync.plugin.clipboard

import com.github.singularity.core.shared.serialization.SyncEvent
import com.github.singularity.core.sync.SyncEventBridge
import com.github.singularity.core.sync.plugin.Plugin
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

private const val PLUGIN_NAME = "clipboard"

class ClipboardPlugin(
    private val syncEventBridge: SyncEventBridge,
) : Plugin {

    override val pluginName = PLUGIN_NAME

    @Serializable
    sealed class Events : SyncEvent {

        override val plugin = PLUGIN_NAME

        @Serializable
        @SerialName("${PLUGIN_NAME}_copied")
        data class Copied(val content: String) : Events()

    }

    override fun handleEvent(event: SyncEvent) {
        when (event) {
            is Events.Copied -> copied(event.content)
        }
    }

    private fun copied(content: String) {

    }

}
