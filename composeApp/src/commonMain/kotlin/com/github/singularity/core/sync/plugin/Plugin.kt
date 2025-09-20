package com.github.singularity.core.sync.plugin

import com.github.singularity.core.shared.model.websocket.SyncEvent

abstract class Plugin(
    broadcastEventCallback: (SyncEvent) -> Unit,
) {

    abstract val pluginName: String

    abstract fun handleEvent(event: SyncEvent)

}
