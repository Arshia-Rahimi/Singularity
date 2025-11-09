package com.github.singularity.ui.feature.connection.client

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.singularity.ui.designsystem.components.PulseAnimation
import com.github.singularity.ui.designsystem.components.TopBar
import com.github.singularity.ui.designsystem.shared.components.dialogs.ConfirmationDialog
import com.github.singularity.ui.designsystem.shared.getPainter
import com.github.singularity.ui.designsystem.shared.getString
import com.github.singularity.ui.feature.connection.client.components.JoinedSyncGroupItem
import com.github.singularity.ui.feature.connection.client.components.ServerItem
import org.koin.compose.viewmodel.koinViewModel
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.available_servers
import singularity.composeapp.generated.resources.discover
import singularity.composeapp.generated.resources.joined_sync_groups
import singularity.composeapp.generated.resources.plus
import singularity.composeapp.generated.resources.searching_dotted
import singularity.composeapp.generated.resources.server
import singularity.composeapp.generated.resources.stop
import singularity.composeapp.generated.resources.switch_to_server

@Composable
fun ClientScreen(
    toggleSyncMode: () -> Unit,
) {
    val viewModel = koinViewModel<ClientViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ClientScreen(
        uiState = uiState,
        execute = {
            when (this) {
                is ClientIntent.ToggleSyncMode -> toggleSyncMode()
                else -> viewModel.execute(this)
            }
        },
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ClientScreen(
    uiState: ClientUiState,
    execute: ClientIntent.() -> Unit,
) {
    var showSwitchModeDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
	        TopBar(
		        title = Res.string.discover.getString(),
                actions = {
                    IconButton(
                        onClick = { showSwitchModeDialog = true },
                    ) {
                        Icon(
                            painter = Res.drawable.server.getPainter(),
                            contentDescription = Res.string.switch_to_server.getString(),
                        )
                    }
                }
            )
        },
    ) { ip ->

        LazyVerticalGrid(
            columns = GridCells.Adaptive(200.dp),
            modifier = Modifier.fillMaxSize()
                .padding(ip)
                .padding(horizontal = 4.dp),
        ) {

            stickyHeader(
                key = "joined_title",
                contentType = "title",
            ) {
                Row(
                    modifier = Modifier
                        .animateItem()
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 12.dp),
                ) {
                    Text(
                        text = Res.string.joined_sync_groups.getString(),
                        fontSize = 20.sp,
                    )
                }
            }

            items(
                items = uiState.joinedSyncGroups,
                key = { it.syncGroupId },
                contentType = { it },
            ) {
                JoinedSyncGroupItem(
                    joinedSyncGroup = it,
                    execute = execute,
                )
            }

            if (uiState.isDiscovering) {
                stickyHeader(
                    key = "available_title",
                    contentType = "title",
                ) {
                    Row(
                        modifier = Modifier
                            .animateItem()
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 12.dp),
                    ) {
                        Text(
                            text = Res.string.available_servers.getString(),
                            fontSize = 20.sp,
                        )
                    }
                }
            } else {
                stickyHeader(
                    key = "discover_title",
                    contentType = "title",
                ) {
                    Row(
                        modifier = Modifier
                            .animateItem()
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        IconButton(
                            onClick = { ClientIntent.StartDiscovery.execute() },
                        ) {
                            Icon(
                                painter = Res.drawable.plus.getPainter(),
                                contentDescription = "discover",
                            )
                        }
                    }
                }
            }

            items(
                items = uiState.availableServers,
                key = { it.syncGroupId },
                contentType = { it },
            ) {
                ServerItem(
                    server = it,
                    execute = execute,
                )
            }

            if (uiState.isDiscovering) {
                stickyHeader(
                    key = "pulse_animation",
                    contentType = "animation",
                ) {
                    Column(
                        modifier = Modifier
                            .animateItem()
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        PulseAnimation(
                            modifier = Modifier.size(150.dp)
                        ) {
                            Text(
                                text = Res.string.searching_dotted.getString(),
                            )
                        }

                        TextButton(
                            onClick = { ClientIntent.StopDiscovery.execute() },
                        ) {
                            Text(
                                text = Res.string.stop.getString(),
                            )
                        }
                    }
                }
            }

        }

        ConfirmationDialog(
            visible = showSwitchModeDialog,
            message = Res.string.switch_to_server.getString(),
            onDismiss = { showSwitchModeDialog = false },
            onConfirm = { ClientIntent.ToggleSyncMode.execute() },
        )

    }
}
