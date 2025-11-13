package com.github.singularity.ui.feature.connection.server

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.singularity.core.shared.model.ServerConnectionState
import com.github.singularity.ui.feature.connection.server.pages.SyncGroupDetails
import com.github.singularity.ui.feature.connection.server.pages.SyncGroupIndex
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ServerScreen() {
    val viewModel = koinViewModel<ServerViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ServerScreen(
        uiState = uiState,
	    execute = { viewModel.execute(this) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ServerScreen(
    uiState: ServerUiState,
    execute: ServerIntent.() -> Unit,
) {

    AnimatedContent(
        targetState = uiState.connectionState,
        modifier = Modifier
            .fillMaxSize(),
    ) {
        when (it) {
	        null -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
		        LinearProgressIndicator()
	        }

            is ServerConnectionState.NoDefaultServer -> SyncGroupIndex(
                uiState = uiState,
                execute = execute,
            )

            is ServerConnectionState.Running -> SyncGroupDetails(
	            connectionState = it,
                execute = execute,
            )
        }
    }

}
