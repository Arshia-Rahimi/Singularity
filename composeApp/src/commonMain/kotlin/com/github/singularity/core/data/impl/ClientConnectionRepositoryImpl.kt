package com.github.singularity.core.data.impl

import com.github.singularity.core.broadcast.DeviceDiscoveryService
import com.github.singularity.core.client.SyncEventRemoteDataSource
import com.github.singularity.core.data.ClientConnectionRepository
import com.github.singularity.core.data.SyncEventBridge
import com.github.singularity.core.database.JoinedSyncGroupsLocalDataSource
import com.github.singularity.core.shared.DISCOVER_TIMEOUT
import com.github.singularity.core.shared.model.ClientConnectionState
import com.github.singularity.core.shared.serialization.SyncEvent
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull

@OptIn(ExperimentalCoroutinesApi::class)
class ClientConnectionRepositoryImpl(
    private val syncEventRemoteDataSource: SyncEventRemoteDataSource,
    syncEventBridge: SyncEventBridge,
    joinedSyncGroupsLocalDataSource: JoinedSyncGroupsLocalDataSource,
    deviceDiscoveryService: DeviceDiscoveryService,
) : ClientConnectionRepository {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val refreshState = MutableSharedFlow<Unit>()

    override val connectionState = refreshState
        .onStart { emit(Unit) }
        .flatMapLatest {
            syncEventRemoteDataSource.disconnect()
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

                        try {
                            syncEventRemoteDataSource.connect(server, defaultServer.authToken)
                        } catch (_: Exception) {
                            emit(ClientConnectionState.ConnectionFailed(server))
                            return@flow
                        }

                        syncEventRemoteDataSource.incomingEventsFlow()
                            .onStart { emit(ClientConnectionState.Connected(server)) }
                            .catch { e ->
                                emit(
                                    ClientConnectionState.ConnectionDropped(server)
                                )
                            }.collect { syncEventBridge.incomingEventCallback(it) }
                    }
                }
        }

    override fun refresh() {
        refreshState.sendPulse(scope)
    }

    override fun send(event: SyncEvent) {
        scope.launch {
            syncEventRemoteDataSource.send(event)
        }
    }

}
