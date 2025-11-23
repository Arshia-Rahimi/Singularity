package com.github.singularity.core.syncservice.plugin.clipboard

import com.github.singularity.core.datasource.SyncEventBridge
import com.github.singularity.core.syncservice.SyncEvent
import com.github.singularity.core.syncservice.plugin.Plugin


class ClipboardPlugin(
    private val syncEventBridge: SyncEventBridge,
) : Plugin {

    override fun handleEvent(syncEvent: SyncEvent) {
        if (syncEvent !is ClipboardEvent) return
        when (syncEvent) {
            is ClipboardEvent.Copied -> copied(syncEvent.clipboardContent)
        }
    }

    private fun copied(clipboardContent: String) {

    }

}
