package com.github.singularity.core.data.impl

import com.github.singularity.core.broadcast.DeviceDiscoveryService
import com.github.singularity.core.client.WebSocketClientDataSource
import com.github.singularity.core.client.utils.WebSocketConnectionDroppedException
import com.github.singularity.core.data.ClientConnectionRepository
import com.github.singularity.core.data.SyncEventRepository
import com.github.singularity.core.database.JoinedSyncGroupDataSource
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

@OptIn(ExperimentalCoroutinesApi::class)
class ClientConnectionRepositoryImpl(
    webSocketClient: WebSocketClientDataSource,
    syncEventRepo: SyncEventRepository,
    joinedSyncGroupsDataSource: JoinedSyncGroupDataSource,
    deviceDiscoveryService: DeviceDiscoveryService,
) : ClientConnectionRepository {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private var isClientRunning = true

    private val refreshState = MutableSharedFlow<Unit>()

    override val connectionState = refreshState.flatMapLatest {
        if (!isClientRunning) flowOf(ConnectionState.Stopped)
        else joinedSyncGroupsDataSource.joinedSyncGroups
            .map { it.firstOrNull { group -> group.isDefault } }
            .flatMapLatest { defaultServer ->
                if (defaultServer == null) flowOf<ConnectionState>(ConnectionState.NoDefaultServer)
                else flow {
                    emit(ConnectionState.Searching(defaultServer))

                    val server = deviceDiscoveryService.discoverServer(defaultServer)

                    if (server == null) {
                        emit(ConnectionState.ServerNotFound(defaultServer, "timeout"))
                    } else {
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
                            }
                            .collect { syncEventRepo.incomingEventCallback(it) }
                    }
                }
            }
    }.shareInWhileSubscribed(1, scope)

    override fun refresh() {
        refreshState.sendPulse()
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
