package com.github.singularity.app.navigation.components

import com.github.singularity.core.shared.util.sendPulse
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

object AppNavigationController {

    private val _toggleDrawerEvent = Channel<Unit>()
    val toggleDrawerEvent = _toggleDrawerEvent.receiveAsFlow()

    fun toggleDrawer() {
        _toggleDrawerEvent.sendPulse()
    }

    private val _popBackStackEvent = Channel<Unit>()
    val popStackEvent = _popBackStackEvent.receiveAsFlow()

    fun popBackStack() {
        _popBackStackEvent.sendPulse()
    }

    private val _canPopBackStack = MutableStateFlow(false)
    val canPopBackStack = _canPopBackStack.asStateFlow()

    fun setCanPopBackStack(isInGraphRoot: Boolean) {
        _canPopBackStack.value = isInGraphRoot
    }

}
