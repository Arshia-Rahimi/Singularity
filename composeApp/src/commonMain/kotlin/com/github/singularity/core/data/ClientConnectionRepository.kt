package com.github.singularity.core.data

import com.github.singularity.core.shared.model.ClientConnectionState
import com.github.singularity.core.shared.serialization.SyncEvent
import kotlinx.coroutines.flow.Flow

interface ClientConnectionRepository {

    val connectionState: Flow<ClientConnectionState>

    suspend fun refresh()

    suspend fun send(event: SyncEvent)

}
