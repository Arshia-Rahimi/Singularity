package com.github.singularity.core.mdns

import kotlinx.coroutines.flow.MutableStateFlow

interface DeviceDiscoveryService {
    
    val devices: MutableStateFlow<List<Device>>
    
    fun release()
    
}