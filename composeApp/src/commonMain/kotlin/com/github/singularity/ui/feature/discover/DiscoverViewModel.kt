package com.github.singularity.ui.feature.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.singularity.core.mdns.DeviceDiscoveryService
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class DiscoverViewModel(
    deviceDiscoveryService: DeviceDiscoveryService,
) : ViewModel() {

    val servers = deviceDiscoveryService.discoverServers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

}
