package com.github.singularity.core.presence

import com.github.singularity.core.shared.model.JoinedSyncGroup
import com.github.singularity.core.shared.model.LocalServer
import kotlinx.coroutines.flow.Flow

interface DeviceDiscoveryService {

    fun discoveredServers(): Flow<List<LocalServer>>

    suspend fun discoverServer(syncGroup: JoinedSyncGroup): LocalServer?
    
}
