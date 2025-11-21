package com.github.singularity.ui.navigation

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

object AppNavigationController {

    private val _toggleDrawerEvent = Channel<Unit>()
    val toggleDrawerEvent = _toggleDrawerEvent.receiveAsFlow()

    fun toggleDrawer() {
	    _toggleDrawerEvent.trySend(Unit)
    }

	private val _navBackEvent = Channel<Unit>()
	val navBackEvent = _navBackEvent.receiveAsFlow()

	fun navigateBack() {
		_navBackEvent.trySend(Unit)
	}

	private val _navigateToEvent = Channel<Route>()
	val navigateToEvent = _navigateToEvent.receiveAsFlow()

	fun navigateTo(route: Route) {
		_navigateToEvent.trySend(route)
	}

}
