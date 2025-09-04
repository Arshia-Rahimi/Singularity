package com.github.singularity.core.client

import com.github.singularity.models.IServer
import com.github.singularity.models.sync.SyncEvent
import kotlinx.coroutines.flow.Flow

interface WebSocketClientDataSource {

    fun connect(server: IServer, authToken: String): Flow<SyncEvent>

    suspend fun send(event: SyncEvent)

}
