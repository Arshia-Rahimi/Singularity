package com.github.singularity.core.sync.plugin

import com.github.singularity.core.shared.serialization.SyncEvent

interface Plugin {

    val pluginName: String

    fun handleEvent(event: SyncEvent)

}
