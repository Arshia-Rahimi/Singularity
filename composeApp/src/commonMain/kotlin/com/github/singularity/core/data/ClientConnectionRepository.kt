package com.github.singularity.core.data

import com.github.singularity.core.syncservice.ClientSyncState
import kotlinx.coroutines.flow.Flow

interface ClientConnectionRepository {

    suspend fun refresh()

    val connectionState: Flow<ClientSyncState>

}
