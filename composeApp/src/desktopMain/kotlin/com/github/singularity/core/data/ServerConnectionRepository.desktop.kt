package com.github.singularity.core.data

import com.github.singularity.core.syncservice.ServerSyncState
import kotlinx.coroutines.flow.Flow

interface ServerConnectionRepository {

    fun runServer(): Flow<ServerSyncState>

    suspend fun refresh()

}
