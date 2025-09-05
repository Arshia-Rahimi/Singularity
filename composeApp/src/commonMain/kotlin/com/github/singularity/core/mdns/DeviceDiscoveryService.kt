package com.github.singularity.core.mdns

import com.github.singularity.core.shared.model.JoinedSyncGroup
import com.github.singularity.core.shared.model.LocalServer
import kotlinx.coroutines.flow.Flow

interface DeviceDiscoveryService {

    fun discoverServers(): Flow<List<LocalServer>>

    suspend fun discoverServer(syncGroup: JoinedSyncGroup): LocalServer?
    
}
