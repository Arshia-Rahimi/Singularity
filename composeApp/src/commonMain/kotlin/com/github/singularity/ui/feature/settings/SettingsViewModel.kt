package com.github.singularity.ui.feature.settings

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.singularity.core.data.PreferencesRepository
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
            themeOption = it[0].theme.themeOption,
			scale = it[0].scale,
		)
	}.stateInWhileSubscribed(SettingsUiState())

	fun execute(intent: SettingsIntent) {
		when (intent) {
            is SettingsIntent.ToggleThemeOption -> toggleThemeOption()
			is SettingsIntent.ChangeScale -> changeScale(intent.scale)
            is SettingsIntent.ChangePrimaryColor -> changePrimaryColor(intent.color)
        }
	}

    private fun toggleThemeOption() {
		viewModelScope.launch {
            preferencesRepo.setAppThemeOption(preferences.value.theme.themeOption.next())
        }
    }

    private fun changePrimaryColor(color: Color) {
        viewModelScope.launch {
            preferencesRepo.changePrimaryColor(color)
        }
    }

	private fun changeScale(scale: Float) {
		viewModelScope.launch {
			preferencesRepo.setScale(scale)
		}
	}

}
