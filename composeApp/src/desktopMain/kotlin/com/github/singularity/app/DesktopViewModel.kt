package com.github.singularity.app

import androidx.lifecycle.ViewModel
import com.github.singularity.core.data.PreferencesRepository
import com.github.singularity.core.shared.util.stateInWhileSubscribed

class DesktopViewModel(
	preferencesRepo: PreferencesRepository,
): ViewModel() {

	val uiState = preferencesRepo.preferences.stateInWhileSubscribed(null)

}
