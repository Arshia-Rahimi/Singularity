package com.github.singularity.core.mdns

import com.github.singularity.core.shared.model.SyncGroup
import kotlinx.coroutines.flow.Flow

interface DeviceDiscoveryService {

    suspend fun broadcastServer(group: SyncGroup)

    suspend fun discoverServers(): Flow<List<Server>>
    
}
