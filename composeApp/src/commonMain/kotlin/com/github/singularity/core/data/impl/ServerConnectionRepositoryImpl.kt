package com.github.singularity.core.data.impl

import com.github.singularity.core.data.ServerConnectionRepository
import com.github.singularity.core.server.KtorWebSocketServer
import com.github.singularity.core.shared.model.HostedSyncGroup

class ServerConnectionRepositoryImpl(
    private val webSocketServer: KtorWebSocketServer,
) : ServerConnectionRepository {

    override suspend fun startServer(group: HostedSyncGroup) {
        webSocketServer.start(group)
    }

    override suspend fun stopServer() {
        webSocketServer.stop()
    }

}
