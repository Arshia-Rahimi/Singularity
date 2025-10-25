package com.github.singularity.ui.feature.home.client

import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.singularity.ui.feature.home.client.pages.DiscoverPage
import com.github.singularity.ui.feature.home.client.pages.JoinedSyncGroupPage
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ClientScreen() {
    val viewModel = koinViewModel<ClientViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ClientScreen(
        uiState = uiState,
        execute = { viewModel.execute(this) },
    )
}

@Composable
private fun ClientScreen(
    uiState: ClientUiState,
    execute: ClientIntent.() -> Unit,
) {
    AnimatedContent(uiState.defaultSyncGroup) {
        when (it) {
            null -> DiscoverPage(uiState, execute)
            else -> JoinedSyncGroupPage(uiState, execute)
        }
    }
}
