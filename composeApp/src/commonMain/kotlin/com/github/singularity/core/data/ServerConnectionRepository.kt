package com.github.singularity.core.data

import com.github.singularity.core.shared.model.websocket.SyncEvent

interface ServerConnectionRepository {

    suspend fun send(event: SyncEvent)

}
