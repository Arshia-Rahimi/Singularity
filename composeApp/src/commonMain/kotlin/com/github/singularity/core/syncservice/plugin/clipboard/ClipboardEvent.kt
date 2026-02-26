package com.github.singularity.core.syncservice.plugin.clipboard

import com.github.singularity.core.syncservice.plugin.SyncEvent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface ClipboardEvent : SyncEvent {

    @Serializable
    @SerialName("Copied")
    data class Copied(val content: String) : ClipboardEvent

    @Serializable
    @SerialName("SendToClipboard")
    data class SendToClipboard(val content: String) : ClipboardEvent

}
