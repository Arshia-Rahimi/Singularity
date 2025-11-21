package com.github.singularity.ui.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class NavigationViewmodel : ViewModel() {

	val backStack = mutableStateListOf<Route>(Route.Connection)

	init {
		listenForEvents()
	}

	private fun listenForEvents() {
		viewModelScope.launch {
			launch {
				AppNavigationController.navBackEvent.collect {
					backStack.removeLastOrNull()
				}
			}
			launch {
				AppNavigationController.navigateToEvent.collect { route ->
					backStack.add(route)
				}
			}
		}
	}

}
