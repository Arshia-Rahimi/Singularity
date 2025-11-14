package com.github.singularity.ui.feature.connection.client

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.singularity.core.shared.model.ClientConnectionState
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
		modifier = Modifier.fillMaxSize(),
	) {
		when (it) {
			null -> LinearLoader()

			is ClientConnectionState.NoDefaultServer -> JoinedSyncGroupIndexPage(
				uiState = uiState,
				execute = execute,
			)

			else -> JoinedSyncGroupDetailsPage(
				connectionState = uiState.connectionState as ClientConnectionState,
				execute = execute,
			)
		}
	}

}
