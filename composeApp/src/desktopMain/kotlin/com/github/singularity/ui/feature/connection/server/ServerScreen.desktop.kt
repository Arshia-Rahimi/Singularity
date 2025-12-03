package com.github.singularity.ui.feature.connection.server

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.singularity.core.syncservice.ServerSyncState
import com.github.singularity.ui.designsystem.components.LinearLoader
import com.github.singularity.ui.feature.connection.server.pages.details.ServerSyncGroupDetailsPage
import com.github.singularity.ui.feature.connection.server.pages.index.ServerSyncGroupIndexPage
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
	    contentKey = {
		    when (it) {
			    is ServerSyncState.Loading -> "Loading"
			    is ServerSyncState.NoDefaultServer -> "NoDefaultServer"
			    is ServerSyncState.Running -> "Running"
		    }
	    },
        modifier = Modifier
            .fillMaxSize(),
    ) {
        when (it) {
            is ServerSyncState.Loading -> LinearLoader()

	        is ServerSyncState.NoDefaultServer -> ServerSyncGroupIndexPage(
                uiState = uiState,
                execute = execute,
            )

	        is ServerSyncState.Running -> ServerSyncGroupDetailsPage(
	            connectionState = it,
                execute = execute,
            )
        }
    }

}
