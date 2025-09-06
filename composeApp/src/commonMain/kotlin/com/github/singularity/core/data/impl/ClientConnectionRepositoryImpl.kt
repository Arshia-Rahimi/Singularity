package com.github.singularity.core.data.impl

import com.github.singularity.core.client.WebSocketClientDataSource
import com.github.singularity.core.client.utils.WebSocketConnectionDroppedException
import com.github.singularity.core.data.ClientConnectionRepository
import com.github.singularity.core.database.JoinedSyncGroupDataSource
import com.github.singularity.core.mdns.DeviceDiscoveryService
import com.github.singularity.core.shared.model.ConnectionState
import com.github.singularity.core.shared.model.websocket.SyncEvent
import com.github.singularity.core.shared.util.onFirst
import com.github.singularity.core.shared.util.sendPulse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn

@OptIn(ExperimentalCoroutinesApi::class)
class ClientConnectionRepositoryImpl(
    private val webSocketClient: WebSocketClientDataSource,
    joinedSyncGroupsDataSource: JoinedSyncGroupDataSource,
    deviceDiscoveryService: DeviceDiscoveryService,
    scope: CoroutineScope,
) : ClientConnectionRepository {

    private val refreshState = MutableSharedFlow<Unit>()

    private val _syncEvents = MutableSharedFlow<SyncEvent>(
        replay = 0,
        extraBufferCapacity = Int.MAX_VALUE,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    override val syncEvents = _syncEvents.asSharedFlow()

    override val connectionState = refreshState.flatMapLatest {
        joinedSyncGroupsDataSource.joinedSyncGroups
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
                            .collect { _syncEvents.tryEmit(it) }
                    }
                }
            }
    }
        .shareIn(scope, SharingStarted.WhileSubscribed(5000), 1)

    override suspend fun send(event: SyncEvent) {
        webSocketClient.send(event)
    }

    override fun refresh() {
        refreshState.sendPulse()
    }

}
