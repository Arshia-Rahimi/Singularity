package com.github.singularity.core.broadcast

import com.github.singularity.core.shared.model.JoinedSyncGroup
import com.github.singularity.core.shared.model.LocalServer
import kotlinx.coroutines.flow.Flow

interface DeviceDiscoveryService {

    val discoveredServers: Flow<List<LocalServer>>

    suspend fun discoverServer(syncGroup: JoinedSyncGroup): LocalServer?
    
}
