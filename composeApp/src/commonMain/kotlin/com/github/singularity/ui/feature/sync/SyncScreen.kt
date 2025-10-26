package com.github.singularity.ui.feature.sync

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SyncScreen() {
    val viewModel = koinViewModel<SyncViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SyncScreen(
        uiState = uiState,
        execute = { viewModel.execute(this) },
    )
}

@Composable
private fun SyncScreen(
    uiState: SyncUiState,
    execute: SyncIntent.() -> Unit,
) {
    Text(uiState.connectionState.message)
}
