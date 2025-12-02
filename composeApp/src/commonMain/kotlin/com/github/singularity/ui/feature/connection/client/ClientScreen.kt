package com.github.singularity.ui.feature.connection.client

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.singularity.core.shared.model.ClientSyncState
import com.github.singularity.ui.designsystem.components.LinearLoader
import com.github.singularity.ui.feature.connection.client.pages.details.JoinedSyncGroupDetailsPage
import com.github.singularity.ui.feature.connection.client.pages.index.JoinedSyncGroupIndexPage
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
	AnimatedContent(
		targetState = uiState.connectionState,
		contentKey = {
			when (it) {
				is ClientSyncState.Loading -> "Loading"
				is ClientSyncState.NoDefaultServer -> "NoDefaultServer"
				is ClientSyncState.WithDefaultServer -> "WithDefaultServer"
			}
		},
		modifier = Modifier.fillMaxSize(),
	) {
		when (it) {
            is ClientSyncState.Loading -> LinearLoader()

            is ClientSyncState.NoDefaultServer -> JoinedSyncGroupIndexPage(
				uiState = uiState,
				execute = execute,
			)

            is ClientSyncState.WithDefaultServer -> JoinedSyncGroupDetailsPage(
                syncState = it,
				execute = execute,
			)
		}
	}

}
