package com.github.singularity.core.mdns

import com.github.singularity.core.common.util.Resource
import com.github.singularity.core.database.entities.JoinedSyncGroup
import kotlinx.coroutines.flow.Flow

interface DeviceDiscoveryService {

    fun discoverServers(): Flow<List<Server>>

    fun discoverServer(syncGroup: JoinedSyncGroup): Flow<Resource<Server>>
    
}
