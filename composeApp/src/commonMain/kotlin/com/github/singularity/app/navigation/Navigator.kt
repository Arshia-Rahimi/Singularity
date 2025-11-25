package com.github.singularity.app.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class Navigator {

	val backStack: SnapshotStateList<Route> = mutableStateListOf<Route>(Route.Connection)

	fun navigateTo(route: Route) {
		backStack.add(route)
	}

	fun navigateUp() {
		backStack.removeLastOrNull()
	}

	private val _toggleDrawerEvent = Channel<Unit>()
	val toggleDrawerEvent = _toggleDrawerEvent.receiveAsFlow()

	fun toggleDrawer() {
		_toggleDrawerEvent.trySend(Unit)
	}

}
