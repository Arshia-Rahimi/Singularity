package com.github.singularity.core.data.impl

import com.github.singularity.core.data.ServerConnectionRepository
import com.github.singularity.core.server.KtorLocalServer
import com.github.singularity.core.shared.model.websocket.SyncEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class ServerConnectionRepositoryImpl(
    private val server: KtorLocalServer,
) : ServerConnectionRepository {

    private val _syncEvents = MutableSharedFlow<SyncEvent>()
    override val syncEvents = _syncEvents.asSharedFlow()

    override suspend fun send(event: SyncEvent) {
        TODO("Not yet implemented")
    }

}
