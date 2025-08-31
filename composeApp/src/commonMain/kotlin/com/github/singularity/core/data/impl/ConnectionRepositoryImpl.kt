package com.github.singularity.core.data.impl

import com.github.singularity.core.data.ConnectionRepository
import com.github.singularity.core.database.LocalJoinedSyncGroupsDataSource
import com.github.singularity.core.mdns.DeviceDiscoveryService
import com.github.singularity.core.shared.model.ConnectionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn

class ConnectionRepositoryImpl(
    joinedSyncGroupsDataSource: LocalJoinedSyncGroupsDataSource,
    private val deviceDiscoveryService: DeviceDiscoveryService,
    scope: CoroutineScope,
) : ConnectionRepository {

    override val connection = joinedSyncGroupsDataSource.joinedSyncGroups
        .map { it.firstOrNull { group -> group.isDefault } }
        .map { defaultServer ->
            if (defaultServer == null) {
                ConnectionState.NoDefaultServer
            } else {
                ConnectionState.Searching(defaultServer)
                val server = deviceDiscoveryService.discoverServer(defaultServer)
                if (server == null) {
                    ConnectionState.ServerNotFound(defaultServer, "time out")
                } else {
                    // todo connect websocket
                    ConnectionState.Connected(server)
                }
            }
        }.shareIn(scope, SharingStarted.WhileSubscribed(5000), 1)

}
