package com.github.singularity.core.data.impl

import com.github.singularity.core.data.PluginEventsRepository
import com.github.singularity.core.shared.model.websocket.SyncEvent
import kotlinx.coroutines.flow.SharedFlow

class PluginEventsRepositoryImpl : PluginEventsRepository {

    override val syncEvents: SharedFlow<SyncEvent>
        get() = TODO("Not yet implemented")

    override suspend fun send(event: SyncEvent) {
        TODO("Not yet implemented")
    }

}
