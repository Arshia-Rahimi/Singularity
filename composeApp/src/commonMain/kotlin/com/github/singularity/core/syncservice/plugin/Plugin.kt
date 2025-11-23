package com.github.singularity.core.syncservice.plugin

interface Plugin {

	fun handleEvent(syncEvent: SyncEvent)

}
