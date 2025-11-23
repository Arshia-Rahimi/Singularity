package com.github.singularity.core.syncservice.plugin

import com.github.singularity.core.shared.model.PluginSettings
import com.github.singularity.core.syncservice.SyncEvent
import kotlinx.coroutines.flow.StateFlow

interface Plugin {

	val settings: StateFlow<PluginSettings>

	fun handleEvent(syncEvent: SyncEvent)

}
