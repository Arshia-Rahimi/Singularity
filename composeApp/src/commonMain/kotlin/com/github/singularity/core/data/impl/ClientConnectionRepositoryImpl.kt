package com.github.singularity.core.data.impl

import com.github.singularity.core.broadcast.DeviceDiscoveryService
import com.github.singularity.core.client.SyncEventRemoteDataSource
import com.github.singularity.core.data.ClientConnectionRepository
import com.github.singularity.core.data.JoinedSyncGroupRepository
import com.github.singularity.core.log.Logger
import com.github.singularity.core.shared.DISCOVER_TIMEOUT
import com.github.singularity.core.shared.model.ClientConnectionState
import com.github.singularity.core.shared.model.ClientSyncState
import com.github.singularity.core.shared.serialization.SyncEvent
import com.github.singularity.core.shared.util.sendPulse
import com.github.singularity.core.sync.SyncEventBridge
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withTimeoutOrNull

@OptIn(ExperimentalCoroutinesApi::class)
class ClientConnectionRepositoryImpl(
	private val syncEventRemoteDataSource: SyncEventRemoteDataSource,
	syncEventBridge: SyncEventBridge,
	joinedSyncGroupRepo: JoinedSyncGroupRepository,
	deviceDiscoveryService: DeviceDiscoveryService,
	logger: Logger,
) : ClientConnectionRepository {

    private val refreshState = MutableSharedFlow<Unit>()

    override val connectionState = refreshState
        .onStart { emit(Unit) }
        .flatMapLatest {
            syncEventRemoteDataSource.disconnect()
            joinedSyncGroupRepo.defaultJoinedSyncGroup
                .flatMapLatest { defaultServer ->
                    if (defaultServer == null) flowOf<ClientSyncState>(ClientSyncState.NoDefaultServer)
                    else flow {
                        emit(
                            ClientSyncState.WithDefaultServer(
                                defaultServer,
                                ClientConnectionState.Searching
                            )
                        )

                        val server = withTimeoutOrNull(DISCOVER_TIMEOUT) {
                            deviceDiscoveryService.discoverServer(defaultServer)
                        }

                        if (server == null) {
                            emit(
                                ClientSyncState.WithDefaultServer(
                                    defaultServer,
                                    ClientConnectionState.ServerNotFound("timeout")
                                )
                            )
                            logger.i(this::class.simpleName, "server not found")
                            return@flow
                        }

                        try {
                            syncEventRemoteDataSource.connect(server, defaultServer.authToken)
                        } catch (e: Exception) {
                            emit(
                                ClientSyncState.WithDefaultServer(
                                    defaultServer,
                                    ClientConnectionState.SyncFailed(server)
                                )
                            )
                            logger.e(this::class.simpleName, "error connecting to server", e)
                            return@flow
                        }

                        syncEventRemoteDataSource.incomingEventsFlow()
	                        .onStart {
                                emit(
                                    ClientSyncState.WithDefaultServer(
                                        defaultServer,
                                        ClientConnectionState.Connected(server)
                                    )
                                )
	                        }
                            .catch { e ->
                                logger.e(this::class.simpleName, "websocket connection timeout", e)
                                emit(
                                    ClientSyncState.WithDefaultServer(
                                        defaultServer,
                                        ClientConnectionState.SyncDropped(server)
                                    )
                                )
                            }.collect { syncEventBridge.incomingEventCallback(it) }
                    }
                }
        }.flowOn(Dispatchers.IO)

    override suspend fun refresh() {
        refreshState.sendPulse()
    }

    override suspend fun send(event: SyncEvent) {
        syncEventRemoteDataSource.send(event)
    }

}
