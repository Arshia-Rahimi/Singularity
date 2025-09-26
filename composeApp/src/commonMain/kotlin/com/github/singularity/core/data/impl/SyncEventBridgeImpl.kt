package com.github.singularity.core.data.impl

import com.github.singularity.core.data.SyncEventBridge
import com.github.singularity.core.shared.serialization.SyncEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class SyncEventBridgeImpl : SyncEventBridge {

    private val _incomingSyncEvents = Channel<SyncEvent>(Channel.BUFFERED)
    override val incomingSyncEvents = _incomingSyncEvents.receiveAsFlow()

    private val _outgoingSyncEvents = Channel<SyncEvent>(Channel.BUFFERED)
    override val outgoingSyncEvents = _outgoingSyncEvents.receiveAsFlow()

    override suspend fun incomingEventCallback(event: SyncEvent) = _incomingSyncEvents.send(event)

    override suspend fun send(event: SyncEvent) = _outgoingSyncEvents.send(event)

}
