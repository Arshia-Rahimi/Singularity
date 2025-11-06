package com.github.singularity.ui.feature.connection

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.singularity.core.shared.SyncMode
import com.github.singularity.core.shared.canHostSyncServer
import com.github.singularity.core.shared.compose.getPainter
import com.github.singularity.core.shared.compose.getString
import com.github.singularity.ui.designsystem.components.dialogs.ConfirmationDialog
import com.github.singularity.ui.feature.connection.client.ClientScreen
import com.github.singularity.ui.feature.connection.server.ServerScreen
import org.koin.compose.viewmodel.koinViewModel
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.client
import singularity.composeapp.generated.resources.switch_to_client

@Composable
fun ConnectionScreen() {
    val viewModel = koinViewModel<ConnectionViewModel>()
    val syncMode by viewModel.syncMode.collectAsStateWithLifecycle()

    var showSwitchModeDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
	        if (canHostSyncServer) {
		        FloatingActionButton(
			        onClick = { showSwitchModeDialog = true },
		        ) {
			        Icon(
				        painter = Res.drawable.client.getPainter(),
				        contentDescription = Res.string.switch_to_client.getString()
			        )
		        }
	        }
        },
    ) { ip ->

        AnimatedContent(
            targetState = syncMode,
            modifier = Modifier.fillMaxSize()
                .padding(ip),
        ) {
            when (it) {
                SyncMode.Server -> ServerScreen()
                SyncMode.Client -> ClientScreen()
            }
        }

        ConfirmationDialog(
            visible = showSwitchModeDialog && canHostSyncServer,
            onConfirm = viewModel::toggleSyncMode,
            onDismiss = { showSwitchModeDialog = false },
            message = Res.string.switch_to_client.getString(),
        )

    }
}
