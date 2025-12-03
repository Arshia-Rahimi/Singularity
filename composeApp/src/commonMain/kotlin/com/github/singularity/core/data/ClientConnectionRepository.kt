package com.github.singularity.core.data

import com.github.singularity.core.syncservice.ClientSyncState
import kotlinx.coroutines.flow.Flow

interface ClientConnectionRepository {

    val connectionState: Flow<ClientSyncState>

    suspend fun refresh()

}
