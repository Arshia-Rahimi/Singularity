package com.github.singularity.core.syncservice.plugin

import com.github.singularity.core.syncservice.SyncEvent

interface Plugin {

	fun handleEvent(syncEvent: SyncEvent)

}
