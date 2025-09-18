package com.github.singularity.core.data.impl

import com.github.singularity.core.broadcast.DeviceDiscoveryService
import com.github.singularity.core.client.SyncEventRemoteDataSource
import com.github.singularity.core.client.WebSocketConnectionDroppedException
import com.github.singularity.core.data.ClientConnectionRepository
import com.github.singularity.core.data.SyncEventRepository
import com.github.singularity.core.database.JoinedSyncGroupsLocalDataSource
import com.github.singularity.core.shared.DISCOVER_TIMEOUT
import com.github.singularity.core.shared.model.ConnectionState
import com.github.singularity.core.shared.util.onFirst
import com.github.singularity.core.shared.util.sendPulse
import com.github.singularity.core.shared.util.shareInWhileSubscribed
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
    webSocketClient: SyncEventRemoteDataSource,
    syncEventRepo: SyncEventRepository,
    joinedSyncGroupsLocalDataSource: JoinedSyncGroupsLocalDataSource,
    deviceDiscoveryService: DeviceDiscoveryService,
) : ClientConnectionRepository {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private var isClientRunning = true

    private val refreshState = MutableSharedFlow<Unit>()

    override val connectionState = refreshState
        .onStart { emit(Unit) }
        .flatMapLatest {
            if (!isClientRunning) flowOf(ConnectionState.Stopped)
            else joinedSyncGroupsLocalDataSource.joinedSyncGroups
                .map { it.firstOrNull { group -> group.isDefault } }
                .flatMapLatest { defaultServer ->
                    if (defaultServer == null) flowOf<ConnectionState>(ConnectionState.NoDefaultServer)
                    else flow {
                        emit(ConnectionState.Searching(defaultServer))

                        val server = withTimeoutOrNull(DISCOVER_TIMEOUT) {
                            deviceDiscoveryService.discoverServer(defaultServer)
                        }

                        if (server == null) {
                            emit(ConnectionState.ServerNotFound(defaultServer, "timeout"))
                            return@flow
                        }

                        webSocketClient.connect(server, defaultServer.authToken)
                            .onFirst { emit(ConnectionState.Connected(server)) }
                            .catch { e ->
                                when (e) {
                                    is WebSocketConnectionDroppedException ->
                                        emit(
                                            ConnectionState.ConnectionFailed(
                                                server,
                                                "connection dropped",
                                            )
                                        )

                                    else ->
                                        emit(
                                            ConnectionState.ConnectionFailed(
                                                server,
                                                "connection failed",
                                            )
                                        )
                                }
                            }.collect { syncEventRepo.incomingEventCallback(it) }
                    }
                }
        }.shareInWhileSubscribed(scope, 1)

    override fun refresh() {
        refreshState.sendPulse(scope)
    }

    override fun startClient() {
        isClientRunning = true
        refresh()
    }

    override fun stopClient() {
        isClientRunning = false
        refresh()
    }

}
