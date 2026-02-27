package com.github.singularity.ui.feature.plugins

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.singularity.core.data.PluginSettingsRepository
import com.github.singularity.core.shared.util.stateInWhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PluginsViewModel(
    private val pluginSettingsRepo: PluginSettingsRepository,
) : ViewModel() {

    val uiState = pluginSettingsRepo.pluginSettings
        .map { pluginSettings ->
            PluginsUiState(
                plugins = pluginSettings.sortedBy { pluginSettings -> pluginSettings.name }
                    .map { it.toPlugin() }
                    .distinctBy { it.name }
            )
        }.stateInWhileSubscribed(PluginsUiState())

    fun execute(intent: PluginsIntent) = when (intent) {
        is PluginsIntent.ToggleIsEnabled -> toggleIsEnabled(intent.pluginName)
    }

    private fun toggleIsEnabled(pluginName: String) {
        viewModelScope.launch {
            pluginSettingsRepo.toggleIsEnabled(pluginName)
        }
    }

}
