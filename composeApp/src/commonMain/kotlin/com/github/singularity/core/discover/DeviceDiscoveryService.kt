package com.github.singularity.core.discover

import kotlinx.coroutines.flow.Flow

interface DeviceDiscoveryService {

    fun discoverServers(): Flow<List<Server>>
    
}
