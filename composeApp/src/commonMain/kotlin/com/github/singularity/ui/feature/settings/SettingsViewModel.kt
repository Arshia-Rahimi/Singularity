package com.github.singularity.ui.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.singularity.core.data.PreferencesRepository
import com.github.singularity.core.shared.AppTheme
import com.github.singularity.core.shared.util.next
import com.github.singularity.core.shared.util.stateInWhileSubscribed
import com.github.singularity.ui.navigation.components.DrawerStateController
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val preferencesRepo: PreferencesRepository,
): ViewModel() {

    private val appTheme = preferencesRepo.preferences
        .map { it.theme }
        .stateInWhileSubscribed(AppTheme.System)

    val uiState = combine(appTheme) {
        SettingsUiState(
            appTheme = it[0],
        )
    }.stateInWhileSubscribed(SettingsUiState())

    fun execute(intent: SettingsIntent) {
        when(intent) {
            is SettingsIntent.ToggleAppTheme -> toggleAppTheme()
            is SettingsIntent.OpenDrawer -> DrawerStateController.openDrawer()
        }
    }

    private fun toggleAppTheme() {
        viewModelScope.launch {
            preferencesRepo.setAppTheme(appTheme.value.next())
        }
    }

}
