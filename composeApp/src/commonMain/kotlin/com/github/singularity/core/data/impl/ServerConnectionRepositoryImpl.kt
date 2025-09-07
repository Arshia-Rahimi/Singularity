package com.github.singularity.core.data.impl

import com.github.singularity.core.data.HostedSyncGroupRepository
import com.github.singularity.core.data.ServerConnectionRepository
import com.github.singularity.core.server.KtorWebSocketServer

class ServerConnectionRepositoryImpl(
    private val webSocketServer: KtorWebSocketServer,
    hostedSyncGroupRepo: HostedSyncGroupRepository,
) : ServerConnectionRepository {

    override val defaultServer = hostedSyncGroupRepo.defaultGroup

    override val isWebSocketServerActive = webSocketServer.isServerRunning

    override val connectedNodes = webSocketServer.connectedNodes

    override fun startServer() {
        webSocketServer.start()
    }

    override fun stopServer() {
        webSocketServer.stop()
    }

}
