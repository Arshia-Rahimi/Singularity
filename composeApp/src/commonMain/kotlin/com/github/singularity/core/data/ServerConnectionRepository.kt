package com.github.singularity.core.data

import com.github.singularity.core.shared.model.ServerSyncState
import kotlinx.coroutines.flow.Flow

interface ServerConnectionRepository {

    fun runServer(): Flow<ServerSyncState>

    suspend fun refresh()

}
