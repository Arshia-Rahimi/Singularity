package com.github.singularity.core.data.impl

import com.github.singularity.core.broadcast.DeviceDiscoveryService
import com.github.singularity.core.client.SyncEventRemoteDataSource
import com.github.singularity.core.client.WebSocketConnectionDroppedException
import com.github.singularity.core.data.ClientConnectionRepository
import com.github.singularity.core.data.SyncEventBridge
import com.github.singularity.core.database.JoinedSyncGroupsLocalDataSource
import com.github.singularity.core.shared.DISCOVER_TIMEOUT
import com.github.singularity.core.shared.model.ClientConnectionState
import com.github.singularity.core.shared.util.sendPulse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withTimeoutOrNull

@OptIn(ExperimentalCoroutinesApi::class)
class ClientConnectionRepositoryImpl(
    syncEventRemoteDataSource: SyncEventRemoteDataSource,
    syncEventBridge: SyncEventBridge,
    joinedSyncGroupsLocalDataSource: JoinedSyncGroupsLocalDataSource,
    deviceDiscoveryService: DeviceDiscoveryService,
) : ClientConnectionRepository {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val refreshState = MutableSharedFlow<Unit>()

    override val connectionState = refreshState
        .onStart { emit(Unit) }
        .flatMapLatest {
            joinedSyncGroupsLocalDataSource.joinedSyncGroups
                .map { it.firstOrNull { group -> group.isDefault } }
                .flatMapLatest { defaultServer ->
                    if (defaultServer == null) flowOf<ClientConnectionState>(ClientConnectionState.NoDefaultServer)
                    else flow {
                        emit(ClientConnectionState.Searching(defaultServer))

                        val server = withTimeoutOrNull(DISCOVER_TIMEOUT) {
                            deviceDiscoveryService.discoverServer(defaultServer)
                        }

                        if (server == null) {
                            emit(ClientConnectionState.ServerNotFound(defaultServer, "timeout"))
                            return@flow
                        }

                        syncEventRemoteDataSource.connect(server, defaultServer.authToken)
                            .onStart { emit(ClientConnectionState.Connected(server)) }
                            .catch { e ->
                                when (e) {
                                    is WebSocketConnectionDroppedException ->
                                        emit(
                                            ClientConnectionState.ConnectionFailed(
                                                server,
                                                "connection dropped",
                                            )
                                        )

                                    else ->
                                        emit(
                                            ClientConnectionState.ConnectionDropped(
                                                server,
                                                "connection failed",
                                            )
                                        )
                                }
                            }.collect { syncEventBridge.incomingEventCallback(it) }
                    }
                }
        }

    override fun refresh() {
        refreshState.sendPulse(scope)
    }

}
