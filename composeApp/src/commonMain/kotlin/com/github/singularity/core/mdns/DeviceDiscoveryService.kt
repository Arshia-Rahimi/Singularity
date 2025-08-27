package com.github.singularity.core.mdns

import com.github.singularity.core.database.entities.JoinedSyncGroup
import com.github.singularity.core.shared.util.Resource
import kotlinx.coroutines.flow.Flow

interface DeviceDiscoveryService {

    fun discoverServers(): Flow<List<Server>>

    fun discoverServer(syncGroup: JoinedSyncGroup): Flow<Resource<Server>>
    
}
