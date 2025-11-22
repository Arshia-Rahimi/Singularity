package com.github.singularity.app

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.singularity.app.navigation.AppNavigationController
import com.github.singularity.app.navigation.Route
import com.github.singularity.core.data.PreferencesRepository
import com.github.singularity.core.shared.util.stateInWhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainViewModel(
	preferencesRepo: PreferencesRepository,
) : ViewModel() {

	val uiState = preferencesRepo.preferences.map { prefs ->
		MainUiState(
			theme = prefs.theme,
			scale = prefs.scale,
		)
	}.stateInWhileSubscribed(MainUiState())

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
