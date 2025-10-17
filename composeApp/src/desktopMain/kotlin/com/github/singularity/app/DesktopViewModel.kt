package com.github.singularity.app

import androidx.lifecycle.ViewModel
import com.github.singularity.core.data.PreferencesRepository
import com.github.singularity.core.shared.util.stateInWhileSubscribed
import kotlinx.coroutines.flow.map

class DesktopViewModel(
	preferencesRepo: PreferencesRepository,
): ViewModel() {

	val scale = preferencesRepo.preferences
		.map { it.scale }
		.stateInWhileSubscribed(null)

}
