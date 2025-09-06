package com.github.singularity.core.data.impl

import com.github.singularity.core.data.ServerConnectionRepository
import com.github.singularity.core.database.HostedSyncGroupNodes
import com.github.singularity.core.server.KtorLocalServer
import com.github.singularity.core.shared.model.Node
import com.github.singularity.core.shared.model.websocket.SyncEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

class ServerConnectionRepositoryImpl(
    private val server: KtorLocalServer,
) : ServerConnectionRepository {

    override val syncEvents: SharedFlow<SyncEvent>
        get() = TODO("Not yet implemented")

    override val nodesJoined: SharedFlow<HostedSyncGroupNodes>
        get() = TODO("Not yet implemented")

    override val nodesConnected: SharedFlow<HostedSyncGroupNodes>
        get() = TODO("Not yet implemented")

    override val requestedNodes: Flow<Node>
        get() = TODO("Not yet implemented")

    override fun approve(node: Node) {
        TODO("Not yet implemented")
    }

    override suspend fun send(event: SyncEvent) {
        TODO("Not yet implemented")
    }

}
