package com.github.singularity.ui.navigation

import androidx.lifecycle.ViewModel
import com.github.singularity.core.data.PreferencesRepository
import com.github.singularity.core.shared.AppTheme
import com.github.singularity.core.shared.util.stateInWhileSubscribed
import kotlinx.coroutines.flow.map

class NavigationViewModel(
    settingsRepo: PreferencesRepository,
) : ViewModel() {

    val appTheme = settingsRepo.preferences.map { it.theme }
        .stateInWhileSubscribed(AppTheme.System)

}
