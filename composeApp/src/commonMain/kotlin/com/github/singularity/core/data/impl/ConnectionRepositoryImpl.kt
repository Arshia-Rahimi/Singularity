package com.github.singularity.core.data.impl

import com.github.singularity.core.client.impl.KtorWebSocketClientDataSource
import com.github.singularity.core.data.ConnectionRepository
import com.github.singularity.core.database.LocalJoinedSyncGroupsDataSource
import com.github.singularity.core.mdns.DeviceDiscoveryService
import com.github.singularity.core.shared.model.ConnectionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn

@OptIn(ExperimentalCoroutinesApi::class)
class ConnectionRepositoryImpl(
    joinedSyncGroupsDataSource: LocalJoinedSyncGroupsDataSource,
    scope: CoroutineScope,
    deviceDiscoveryService: DeviceDiscoveryService,
    webSocketClientDataSource: KtorWebSocketClientDataSource,
) : ConnectionRepository {

    override val connection = joinedSyncGroupsDataSource.joinedSyncGroups
        .map { it.firstOrNull { group -> group.isDefault } }
        .flatMapLatest { defaultServer ->
            if (defaultServer == null) flow { emit(ConnectionState.NoDefaultServer) }
            else flow {
                emit(ConnectionState.Searching(defaultServer))

                val server = deviceDiscoveryService.discoverServer(defaultServer)
                if (server == null) {
                    emit(ConnectionState.ServerNotFound(defaultServer, "timeout"))
                } else {
                    // todo
                    emit(ConnectionState.Connected(server))
                }
            }
        }.shareIn(scope, SharingStarted.WhileSubscribed(5000), 1)

}
