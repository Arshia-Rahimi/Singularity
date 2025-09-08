package com.github.singularity.core.data.impl

import com.github.singularity.core.data.HostedSyncGroupRepository
import com.github.singularity.core.data.ServerConnectionRepository
import com.github.singularity.core.server.KtorWebSocketServer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

class ServerConnectionRepositoryImpl(
    private val webSocketServer: KtorWebSocketServer,
    hostedSyncGroupRepo: HostedSyncGroupRepository,
) : ServerConnectionRepository {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val defaultGroup = hostedSyncGroupRepo.defaultGroup
        .apply {
            onEach {
                stopServer()
                if (it != null && webSocketServer.isServerRunning.value) startServer()
            }.launchIn(scope)
        }.stateIn(scope, SharingStarted.WhileSubscribed(5000), null)

    override val currentServer = webSocketServer.isServerRunning
        .map { if (it) defaultGroup.value else null }
        .stateIn(scope, SharingStarted.WhileSubscribed(5000), null)

    override val connectedNodes = webSocketServer.connectedNodes

    override fun startServer() {
        webSocketServer.start()
    }

    override fun stopServer() {
        webSocketServer.stop()
    }

}
