package com.github.singularity.core.syncservice.plugin

import com.github.singularity.core.syncservice.SyncEvent

interface Plugin : PluginSettingsManager {

	fun handleEvent(syncEvent: SyncEvent)

}
