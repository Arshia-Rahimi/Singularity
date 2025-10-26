package com.github.singularity.core.data.impl

import com.github.singularity.core.data.HostedSyncGroupRepository
import com.github.singularity.core.data.ServerConnectionRepository
import com.github.singularity.core.server.KtorWebSocketServer
import com.github.singularity.core.shared.model.ServerConnectionState
import com.github.singularity.core.shared.util.sendPulse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

@OptIn(ExperimentalCoroutinesApi::class)
class ServerConnectionRepositoryImpl(
    private val webSocketServer: KtorWebSocketServer,
    private val hostedSyncGroupRepo: HostedSyncGroupRepository,
) : ServerConnectionRepository {

    private val refreshState = MutableSharedFlow<Unit>()

    override fun runServer() = refreshState
        .onStart { emit(Unit) }
        .flatMapLatest {
            hostedSyncGroupRepo.defaultSyncGroup
                .flatMapLatest { group ->
                    if (group == null) flowOf(ServerConnectionState.NoDefaultServer)
                    else {
                        webSocketServer.start()
                        webSocketServer.connectedNodes.map { nodes ->
                            ServerConnectionState.Running(group, nodes)
                        }
                    }
                }
        }.onCompletion {
            webSocketServer.stop()
        }.flowOn(Dispatchers.IO)

    override suspend fun refresh() {
        refreshState.sendPulse()
    }

}
