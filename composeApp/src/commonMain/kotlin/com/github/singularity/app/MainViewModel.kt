package com.github.singularity.app

import androidx.lifecycle.ViewModel
import com.github.singularity.core.data.PreferencesRepository
import com.github.singularity.core.shared.util.stateInWhileSubscribed
import kotlinx.coroutines.flow.map

class MainViewModel(
	preferencesRepo: PreferencesRepository,
) : ViewModel() {

	val uiState = preferencesRepo.preferences.map { prefs ->
		MainUiState(
			theme = prefs.theme,
			scale = prefs.scale,
		)
	}.stateInWhileSubscribed(MainUiState())

}
