package com.github.singularity.core.data.impl

import com.github.singularity.core.data.HostedSyncGroupRepository
import com.github.singularity.core.data.ServerConnectionRepository
import com.github.singularity.core.server.KtorWebSocketServer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ServerConnectionRepositoryImpl(
    private val webSocketServer: KtorWebSocketServer,
    hostedSyncGroupRepo: HostedSyncGroupRepository,
    scope: CoroutineScope,
) : ServerConnectionRepository {

    override val defaultServer = hostedSyncGroupRepo.defaultGroup
        .apply {
            onEach {
                if (isWebSocketServerActive.value) {
                    stopServer()
                    startServer()
                }
            }.launchIn(scope)
        }

    override val isWebSocketServerActive = webSocketServer.isServerRunning

    override val connectedNodes = webSocketServer.connectedNodes

    override fun startServer() {
        webSocketServer.start()
    }

    override fun stopServer() {
        webSocketServer.stop()
    }

}
