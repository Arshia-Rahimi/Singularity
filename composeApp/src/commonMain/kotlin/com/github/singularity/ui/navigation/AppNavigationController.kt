package com.github.singularity.ui.navigation

import com.github.singularity.core.shared.util.sendPulse
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

object AppNavigationController {

    private val _toggleDrawerEvent = Channel<Unit>()
    val toggleDrawerEvent = _toggleDrawerEvent.receiveAsFlow()

    fun toggleDrawer() {
        _toggleDrawerEvent.sendPulse()
    }

}
