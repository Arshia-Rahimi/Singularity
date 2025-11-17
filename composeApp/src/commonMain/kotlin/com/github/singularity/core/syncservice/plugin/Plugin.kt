package com.github.singularity.core.syncservice.plugin

import com.github.singularity.core.syncservice.SyncEventData

interface Plugin {

    val pluginName: String

    fun handleEvent(event: SyncEventData)

}
