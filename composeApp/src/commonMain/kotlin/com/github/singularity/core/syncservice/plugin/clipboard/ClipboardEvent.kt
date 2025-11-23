package com.github.singularity.core.syncservice.plugin.clipboard

import com.github.singularity.core.syncservice.plugin.SyncEvent
import kotlinx.serialization.Serializable

@Serializable
sealed class ClipboardEvent : SyncEvent {

    override val pluginName = ClipboardPlugin::class.simpleName!!

    @Serializable
    data class Copied(
        val clipboardContent: String,
    ) : ClipboardEvent()

}
