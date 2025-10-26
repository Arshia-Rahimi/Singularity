package com.github.singularity.ui.feature.home.client

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.singularity.core.shared.canHostSyncServer
import com.github.singularity.core.shared.compose.getPainter
import com.github.singularity.core.shared.compose.getString
import com.github.singularity.core.shared.model.ClientConnectionState
import com.github.singularity.ui.designsystem.components.ScreenScaffold
import com.github.singularity.ui.designsystem.components.dialogs.ConfirmationDialog
import com.github.singularity.ui.feature.home.client.pages.DiscoverPage
import com.github.singularity.ui.feature.home.client.pages.JoinedSyncGroupPage
import org.koin.compose.viewmodel.koinViewModel
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.server
import singularity.composeapp.generated.resources.switch_to_server

@Composable
fun ClientScreen() {
	val viewModel = koinViewModel<ClientViewModel>()
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()

	ClientScreen(
		uiState = uiState,
		execute = { viewModel.execute(this) },
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ClientScreen(
	uiState: ClientUiState,
	execute: ClientIntent.() -> Unit,
) {
	var showSwitchModeDialog by remember { mutableStateOf(false) }
	var topBar by remember { mutableStateOf<@Composable () -> Unit>({}) }

	ScreenScaffold(
		topBar = topBar,
		floatingActionButton = {
			if (canHostSyncServer) {
				FloatingActionButton(
					onClick = { showSwitchModeDialog = true },
				) {
					Icon(
						painter = Res.drawable.server.getPainter(),
						contentDescription = Res.string.switch_to_server.getString()
					)
				}
			}
		},
	) { ip ->

		AnimatedContent(
			targetState = uiState.connectionState,
			modifier = Modifier
				.fillMaxSize()
				.padding(ip),
		) { state ->
			when (state) {
				is ClientConnectionState.NoDefaultServer -> DiscoverPage(
					uiState = uiState,
					execute = execute,
					topBar = { topBar = it },
				)

				else -> JoinedSyncGroupPage(
					uiState = uiState,
					execute = execute,
					topBar = { topBar = it },
				)
			}
		}

		ConfirmationDialog(
			visible = showSwitchModeDialog && canHostSyncServer,
			onConfirm = { ClientIntent.ToggleSyncMode.execute() },
			onDismiss = { showSwitchModeDialog = false },
			message = Res.string.switch_to_server.getString(),
		)
	}
}
