package com.github.singularity.ui.feature.broadcast

import androidx.lifecycle.ViewModel
import com.github.singularity.core.mdns.DeviceBroadcastService

class BroadcastViewModel(
    private val deviceBroadcastService: DeviceBroadcastService,
) : ViewModel() {

}
