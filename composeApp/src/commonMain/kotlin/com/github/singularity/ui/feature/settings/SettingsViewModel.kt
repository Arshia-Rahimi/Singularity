package com.github.singularity.ui.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.singularity.core.data.PreferencesRepository
import com.github.singularity.core.shared.AppTheme
import com.github.singularity.core.shared.util.next
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val preferencesRepo: PreferencesRepository,
): ViewModel() {

    private val appTheme = preferencesRepo.preferences
        .map { it.theme }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppTheme.System)

    val uiState = combine(appTheme) {
        SettingsUiState(
            appTheme = it[0],
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SettingsUiState())

    fun execute(intent: SettingsIntent) {
        when(intent) {
            is SettingsIntent.ToggleApptheme -> toggleAppTheme()
            SettingsIntent.NavBack -> Unit
        }
    }

    private fun toggleAppTheme() {
        viewModelScope.launch {
            preferencesRepo.setAppTheme(appTheme.value.next())
        }
    }

}
