package com.github.singularity.core.syncservice.plugin

interface Plugin {

	val pluginName: String

	fun handleEvent(event: SyncEvent)

}
