package com.github.singularity.core.sync.plugin

import com.github.singularity.core.sync.SyncEventData

interface Plugin {

    val pluginName: String

    fun handleEvent(event: SyncEventData)

}
