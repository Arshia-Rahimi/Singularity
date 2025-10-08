package com.github.singularity.app.navigation.components

import com.github.singularity.core.shared.util.sendPulse
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

object AppStateController {

    private val _openDrawerEvent = Channel<Unit>()
    val openDrawerEvent = _openDrawerEvent.receiveAsFlow()

    fun openDrawer() {
        _openDrawerEvent.sendPulse()
    }

}
