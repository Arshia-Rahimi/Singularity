package com.github.singularity.ui.feature.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.github.singularity.core.shared.SyncMode
import com.github.singularity.core.shared.canHostSyncServer
import com.github.singularity.core.shared.compose.getPainter
import com.github.singularity.core.shared.compose.getString
import com.github.singularity.ui.designsystem.components.dialogs.ConfirmationDialog
import com.github.singularity.ui.feature.main.components.broadcast.BroadcastSection
import com.github.singularity.ui.feature.main.components.discover.DiscoverSection
import org.koin.compose.viewmodel.koinViewModel
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.broadcast
import singularity.composeapp.generated.resources.client
import singularity.composeapp.generated.resources.discover
import singularity.composeapp.generated.resources.refresh
import singularity.composeapp.generated.resources.server
import singularity.composeapp.generated.resources.settings
import singularity.composeapp.generated.resources.switch_to_client
import singularity.composeapp.generated.resources.switch_to_server
import kotlin.time.ExperimentalTime

@Composable
fun MainScreen(
    toSettingsScreen: () -> Unit,
) {
    val viewModel = koinViewModel<MainViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MainScreen(
        uiState = uiState,
        execute = {
            when (this) {
                is MainIntent.ToSettingsScreen -> toSettingsScreen()
                else -> viewModel.execute(this)
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
private fun MainScreen(
    uiState: MainUiState,
    execute: MainIntent.() -> Unit,
) {
    var showSwitchModeDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    AnimatedContent(uiState.syncMode) {
                        if (it == SyncMode.Client) Text(Res.string.discover.getString())
                        else Text(Res.string.broadcast.getString())
                    }
                },
                actions = {
                    IconButton(
                        onClick = { MainIntent.ToSettingsScreen.execute() },
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
            if (canHostSyncServer) {
                IconButton(
                    onClick = { showSwitchModeDialog = true },
                ) {
                    if (uiState.syncMode == SyncMode.Client) {
                        Icon(
                            painter = Res.drawable.server.getPainter(),
                            contentDescription = Res.string.switch_to_server.getString()
                        )
                    } else {
                        Icon(
                            painter = Res.drawable.client.getPainter(),
                            contentDescription = Res.string.switch_to_client.getString()
                        )
                    }
                }
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
                    onClick = { MainIntent.RefreshConnection.execute() },
                ) {
                    Icon(
                        painter = Res.drawable.refresh.getPainter(),
                        contentDescription = Res.string.refresh.getString(),
                    )
                }
            }

            AnimatedContent(uiState.syncMode) {
                if (it == SyncMode.Client) {
                    DiscoverSection(
                        uiState = uiState.discoverUiState,
                        execute = execute,
                    )
                } else {
                    BroadcastSection(
                        uiState = uiState.broadcastUiState,
                        execute = execute,
                    )
                }
            }

            ConfirmationDialog(
                visible = showSwitchModeDialog && canHostSyncServer,
                onConfirm = { MainIntent.ToggleSyncMode.execute() },
                onDismiss = { showSwitchModeDialog = false },
                message = if (uiState.syncMode == SyncMode.Client)
                    Res.string.switch_to_server.getString()
                else Res.string.switch_to_client.getString(),
            )

        }
    }
}
