package com.github.singularity.ui.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.singularity.app.navigation.components.AppNavigationController
import com.github.singularity.core.data.PreferencesRepository
import com.github.singularity.core.shared.ScaleOption
import com.github.singularity.core.shared.model.PreferencesModel
import com.github.singularity.core.shared.util.next
import com.github.singularity.core.shared.util.stateInWhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class SettingsViewModel(
	private val preferencesRepo: PreferencesRepository,
) : ViewModel() {

	private val preferences = preferencesRepo.preferences
		.stateInWhileSubscribed(PreferencesModel())

	val uiState = combine(preferences) {
		SettingsUiState(
			theme = it[0].theme,
			scale = it[0].scale,
		)
	}.stateInWhileSubscribed(SettingsUiState())

	fun execute(intent: SettingsIntent) {
		when (intent) {
			is SettingsIntent.ToggleTheme -> toggleTheme()
			is SettingsIntent.OpenDrawer -> AppNavigationController.toggleDrawer()
			is SettingsIntent.ChangeScale -> changeScale(intent.scale)
		}
	}

	private fun toggleTheme() {
		viewModelScope.launch {
			preferencesRepo.setAppTheme(preferences.value.theme.next())
		}
	}

	private fun changeScale(scale: ScaleOption) {
		viewModelScope.launch {
			preferencesRepo.setScale(scale)
		}
	}

}
