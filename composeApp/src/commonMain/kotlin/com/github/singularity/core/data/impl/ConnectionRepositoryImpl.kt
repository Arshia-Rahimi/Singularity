package com.github.singularity.core.data.impl

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
) : ConnectionRepository {

    override val connection = joinedSyncGroupsDataSource.joinedSyncGroups
        .map { it.firstOrNull { group -> group.isDefault } }
        .flatMapLatest { defaultServer ->
            if (defaultServer == null) return@flatMapLatest flow { emit(null) }
            ConnectionState.Searching(defaultServer)
            val server = deviceDiscoveryService.discoverServer(defaultServer)
            if (server == null) {
                ConnectionState.ServerNotFound(defaultServer, "time out")
            } else {
                // todo connect websocket
                ConnectionState.Connected(server)
            }
        }.shareIn(scope, SharingStarted.WhileSubscribed(5000), 1)

}
