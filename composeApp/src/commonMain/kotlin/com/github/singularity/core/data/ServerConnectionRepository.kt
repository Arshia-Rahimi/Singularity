package com.github.singularity.core.data

import com.github.singularity.core.shared.model.websocket.SyncEvent
import kotlinx.coroutines.flow.SharedFlow

interface ServerConnectionRepository {

    val syncEvents: SharedFlow<SyncEvent>

    suspend fun send(event: SyncEvent)

}
