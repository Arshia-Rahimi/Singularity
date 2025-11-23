package com.github.singularity.ui.feature.plugins

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PluginsScreen() {
	val viewModel = koinViewModel<PluginsViewModel>()
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()

	PluginsScreen(
		uiState = uiState,
		execute = { viewModel.execute(this) },
	)
}

@Composable
private fun PluginsScreen(
	uiState: PluginsUiState,
	execute: PluginsIntent.() -> Unit,
) {

}
