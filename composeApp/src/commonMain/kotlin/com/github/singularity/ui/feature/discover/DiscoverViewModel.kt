package com.github.singularity.ui.feature.discover

import androidx.lifecycle.ViewModel
import com.github.singularity.core.data.DeviceDiscoveryRepository

class DiscoverViewModel(
    private val deviceDiscoveryRepo: DeviceDiscoveryRepository,
) : ViewModel() {

    val devices = deviceDiscoveryRepo.devices

    override fun onCleared() {
        deviceDiscoveryRepo.release()
        super.onCleared()
    }

}

