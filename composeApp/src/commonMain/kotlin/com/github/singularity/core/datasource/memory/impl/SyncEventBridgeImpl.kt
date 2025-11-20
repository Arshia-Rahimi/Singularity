package com.github.singularity.core.datasource.memory.impl

import com.github.singularity.core.datasource.memory.SyncEventBridge
import com.github.singularity.core.syncservice.plugin.SyncEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class SyncEventBridgeImpl : SyncEventBridge {

	private val _incomingSyncEvents = MutableSharedFlow<SyncEvent>()
	override val incomingSyncEvents = _incomingSyncEvents.asSharedFlow()

	private val _outgoingSyncEvents = MutableSharedFlow<SyncEvent>()
	override val outgoingSyncEvents = _outgoingSyncEvents.asSharedFlow()

	override suspend fun incomingEventCallback(event: SyncEvent) = _incomingSyncEvents.emit(event)

	override suspend fun send(event: SyncEvent) = _outgoingSyncEvents.emit(event)

}
