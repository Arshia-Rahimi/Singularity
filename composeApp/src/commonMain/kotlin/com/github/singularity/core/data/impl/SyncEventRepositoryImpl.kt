package com.github.singularity.core.data.impl

import com.github.singularity.core.data.SyncEventRepository
import com.github.singularity.core.shared.model.websocket.SyncEvent
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class SyncEventRepositoryImpl : SyncEventRepository {

    private val _incomingSyncEvents = MutableSharedFlow<SyncEvent>(
        replay = 0,
        extraBufferCapacity = Int.MAX_VALUE,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    override val incomingSyncEvents = _incomingSyncEvents.asSharedFlow()

    private val _outgoingSyncEvents = MutableSharedFlow<SyncEvent>(
        replay = 0,
        extraBufferCapacity = Int.MAX_VALUE,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    override val outgoingSyncEvents = _outgoingSyncEvents.asSharedFlow()

    override suspend fun incomingEventCallback(event: SyncEvent) =
        _incomingSyncEvents.emit(event)

    override suspend fun send(event: SyncEvent) =
        _outgoingSyncEvents.emit(event)

}
