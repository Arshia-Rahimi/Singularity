package com.github.singularity.core.mdns

import kotlinx.coroutines.flow.Flow

interface DeviceDiscoveryService {

    fun discoverServers(): Flow<List<Server>>
    
}
