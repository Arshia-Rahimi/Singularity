package com.github.singularity.ui.feature.permissions

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PermissionsViewModel : ViewModel() {

    val uiState = MutableStateFlow(PermissionUiState()).asStateFlow()

    fun execute(intent: PermissionsIntent) {
        when (intent) {
            else -> {}
        }
    }

}
