package com.github.singularity.core.data

import com.github.singularity.core.shared.model.ConnectionState
import com.github.singularity.models.sync.SyncEvent
import kotlinx.coroutines.flow.SharedFlow

interface ConnectionRepository {

    val syncEvents: SharedFlow<SyncEvent>

    val connectionState: SharedFlow<ConnectionState>

    suspend fun send(event: SyncEvent)

    fun refresh()

}
