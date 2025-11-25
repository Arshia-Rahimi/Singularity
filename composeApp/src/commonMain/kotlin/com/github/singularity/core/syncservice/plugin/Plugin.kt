package com.github.singularity.core.syncservice.plugin

import com.github.singularity.core.syncservice.SyncEvent

interface Plugin : PluginSettingsManager {

	val pluginName: String

	fun handleEvent(syncEvent: SyncEvent)

}
