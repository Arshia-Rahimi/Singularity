package com.github.singularity.ui.feature.publish

import androidx.lifecycle.ViewModel
import com.github.singularity.core.mdns.DeviceDiscoveryService

class PublishViewModel(
    private val deviceDiscoveryService: DeviceDiscoveryService,
) : ViewModel() {

    override fun onCleared() {
        deviceDiscoveryService.release()
        super.onCleared()
    }

}
