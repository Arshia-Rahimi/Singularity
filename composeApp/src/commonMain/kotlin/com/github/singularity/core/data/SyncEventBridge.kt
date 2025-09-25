package com.github.singularity.core.data

import com.github.singularity.core.shared.model.websocket.SyncEvent
import kotlinx.coroutines.flow.SharedFlow

interface SyncEventBridge {

    val incomingSyncEvents: SharedFlow<SyncEvent>

    val outgoingSyncEvents: SharedFlow<SyncEvent>

    suspend fun incomingEventCallback(event: SyncEvent)

    suspend fun send(event: SyncEvent)

}
