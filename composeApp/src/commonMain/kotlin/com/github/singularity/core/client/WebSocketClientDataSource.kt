package com.github.singularity.core.client

import com.github.singularity.core.shared.model.LocalServer
import com.github.singularity.core.shared.model.websocket.SyncEvent
import kotlinx.coroutines.flow.Flow

interface WebSocketClientDataSource {

    fun connect(server: LocalServer, authToken: String): Flow<SyncEvent>

    suspend fun send(event: SyncEvent)

}
