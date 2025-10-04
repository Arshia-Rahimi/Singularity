package com.github.singularity.ui.feature.broadcast

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.singularity.core.shared.canHostSyncServer
import com.github.singularity.core.shared.compose.getPainter
import com.github.singularity.core.shared.compose.getString
import com.github.singularity.ui.designsystem.components.dialogs.ConfirmationDialog
import com.github.singularity.ui.feature.broadcast.broadcast.BroadcastSection
import org.koin.compose.viewmodel.koinViewModel
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.broadcast
import singularity.composeapp.generated.resources.client
import singularity.composeapp.generated.resources.refresh
import singularity.composeapp.generated.resources.settings
import singularity.composeapp.generated.resources.switch_to_client

@Composable
fun BroadcastScreen(
    toSettingsScreen: () -> Unit,
) {
    val viewModel = koinViewModel<BroadcastViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BroadcastScreen(
        uiState = uiState,
        execute = {
            when (this) {
                is BroadcastIntent.ToSettingsScreen -> toSettingsScreen()
                else -> viewModel.execute(this)
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BroadcastScreen(
    uiState: BroadcastUiState,
    execute: BroadcastIntent.() -> Unit,
) {
    var showSwitchModeDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(Res.string.broadcast.getString())
                },
                actions = {
                    IconButton(
                        onClick = { BroadcastIntent.ToSettingsScreen.execute() },
                    ) {
                        Icon(
                            painter = Res.drawable.settings.getPainter(),
                            contentDescription = Res.string.settings.getString(),
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showSwitchModeDialog = true },
            ) {
                Icon(
                    painter = Res.drawable.client.getPainter(),
                    contentDescription = Res.string.switch_to_client.getString()
                )
            }
        },
    ) { ip ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(ip)
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                AnimatedContent(
                    targetState = uiState.connectionState.message,
                ) {
                    Text(it)
                }

                IconButton(
                    onClick = { BroadcastIntent.RefreshConnection.execute() },
                ) {
                    Icon(
                        painter = Res.drawable.refresh.getPainter(),
                        contentDescription = Res.string.refresh.getString(),
                    )
                }
            }

            BroadcastSection(
                uiState = uiState,
                execute = execute,
            )

            ConfirmationDialog(
                visible = showSwitchModeDialog && canHostSyncServer,
                onConfirm = { BroadcastIntent.ToggleSyncMode.execute() },
                onDismiss = { showSwitchModeDialog = false },
                message = Res.string.switch_to_client.getString(),
            )
        }
    }
}
