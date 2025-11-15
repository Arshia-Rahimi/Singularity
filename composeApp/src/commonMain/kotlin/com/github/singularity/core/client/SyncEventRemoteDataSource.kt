package com.github.singularity.core.client

import com.github.singularity.core.shared.model.LocalServer
import com.github.singularity.core.sync.SyncEvent
import kotlinx.coroutines.flow.Flow

interface SyncEventRemoteDataSource {

    suspend fun connect(server: LocalServer, token: String)

    suspend fun disconnect()

    fun incomingEventsFlow(): Flow<SyncEvent>

    suspend fun send(event: SyncEvent)

}
