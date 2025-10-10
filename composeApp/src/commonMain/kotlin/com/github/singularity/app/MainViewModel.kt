package com.github.singularity.app

import androidx.lifecycle.ViewModel
import com.github.singularity.core.data.PreferencesRepository
import com.github.singularity.core.shared.util.stateInWhileSubscribed
import kotlinx.coroutines.flow.map

class MainViewModel(
    preferencesRepo: PreferencesRepository,
) : ViewModel() {

    val uiState = preferencesRepo.preferences.map {
        MainUiState(
            theme = it.theme,
            syncMode = it.syncMode,
            scale = it.scale,
        )
    }.stateInWhileSubscribed(null)

}
