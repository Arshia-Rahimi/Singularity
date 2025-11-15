package com.github.singularity.core.data

import com.github.singularity.core.shared.model.ClientSyncState
import com.github.singularity.core.sync.SyncEvent
import kotlinx.coroutines.flow.Flow

interface ClientConnectionRepository {

    val connectionState: Flow<ClientSyncState>

    suspend fun refresh()

    suspend fun send(event: SyncEvent)

}
