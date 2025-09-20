package com.github.singularity.core.data.impl

import com.github.singularity.core.data.HostedSyncGroupRepository
import com.github.singularity.core.data.ServerConnectionRepository
import com.github.singularity.core.server.KtorWebSocketServer
import com.github.singularity.core.shared.model.ServerConnectionState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion

@OptIn(ExperimentalCoroutinesApi::class)
class ServerConnectionRepositoryImpl(
    private val webSocketServer: KtorWebSocketServer,
    private val hostedSyncGroupRepo: HostedSyncGroupRepository,
) : ServerConnectionRepository {

    override fun runServer() = hostedSyncGroupRepo.defaultSyncGroup.flatMapLatest { group ->
        webSocketServer.stop()
        if (group == null) flowOf(ServerConnectionState.NoDefaultServer)
        else {
            webSocketServer.start(group)
            webSocketServer.connectedNodes.map { nodes ->
                ServerConnectionState.Running(group, nodes)
            }.onCompletion {
                webSocketServer.stop()
            }
        }
    }

}
