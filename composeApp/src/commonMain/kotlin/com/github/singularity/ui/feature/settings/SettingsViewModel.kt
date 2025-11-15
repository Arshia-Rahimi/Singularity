package com.github.singularity.ui.feature.settings

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.singularity.core.data.PreferencesRepository
import com.github.singularity.core.shared.util.next
import com.github.singularity.core.shared.util.stateInWhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val preferencesRepo: PreferencesRepository,
) : ViewModel() {

    val uiState = preferencesRepo.preferences.map {
        SettingsUiState(
            themeOption = it.theme.themeOption,
            scale = it.scale,
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
            preferencesRepo.setAppThemeOption(uiState.value.themeOption.next())
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
