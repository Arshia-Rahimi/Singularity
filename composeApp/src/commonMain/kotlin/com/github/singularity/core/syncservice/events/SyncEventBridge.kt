package com.github.singularity.core.syncservice.events

import kotlinx.coroutines.flow.Flow

interface SyncEventBridge {

    val incomingSyncEvents: Flow<SyncEvent>

    val outgoingSyncEvents: Flow<SyncEvent>

    suspend fun incomingEventCallback(event: SyncEvent)

    suspend fun send(event: SyncEvent)

}