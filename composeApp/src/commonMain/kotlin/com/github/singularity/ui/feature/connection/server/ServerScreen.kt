package com.github.singularity.ui.feature.connection.server

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.singularity.core.shared.canHostSyncServer
import com.github.singularity.core.shared.compose.getPainter
import com.github.singularity.core.shared.compose.getString
import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.ui.designsystem.components.DrawerIcon
import com.github.singularity.ui.designsystem.components.ScreenScaffold
import com.github.singularity.ui.designsystem.components.dialogs.ConfirmationDialog
import com.github.singularity.ui.designsystem.components.dialogs.InputDialog
import com.github.singularity.ui.feature.connection.server.components.HostedSyncGroupItem
import com.github.singularity.ui.feature.connection.server.components.NodeItem
import org.koin.compose.viewmodel.koinViewModel
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.available_sync_groups
import singularity.composeapp.generated.resources.broadcast
import singularity.composeapp.generated.resources.client
import singularity.composeapp.generated.resources.create
import singularity.composeapp.generated.resources.create_new_sync_group
import singularity.composeapp.generated.resources.plus
import singularity.composeapp.generated.resources.switch_to_client

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
    var showSwitchModeDialog by remember { mutableStateOf(false) }
    var showCreateGroupDialog by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    ScreenScaffold(
        topBar = {
            TopAppBar(
                title = { Text(Res.string.broadcast.getString()) },
                navigationIcon = {
                    DrawerIcon { ServerIntent.OpenDrawer.execute() }
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
       
        Content(
            ip = ip,
            uiState = uiState,
            execute = execute,
            showCreateGroupDialog = { showCreateGroupDialog = true },
        )

        InputDialog(
            visible = showCreateGroupDialog,
            onConfirm = { ServerIntent.CreateGroup(it).execute() },
            onDismiss = {
                showCreateGroupDialog = false
                focusManager.clearFocus()
            },
            confirmText = Res.string.create.getString(),
            title = Res.string.create_new_sync_group.getString(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        )

        ConfirmationDialog(
            visible = showSwitchModeDialog && canHostSyncServer,
            onConfirm = { ServerIntent.ToggleSyncMode.execute() },
            onDismiss = { showSwitchModeDialog = false },
            message = Res.string.switch_to_client.getString(),
        )

    }
}

@Composable
private fun Content(
    showCreateGroupDialog: () -> Unit,
    ip: PaddingValues,
    uiState: ServerUiState,
    execute: ServerIntent.() -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(200.dp),
        modifier = Modifier.fillMaxSize()
            .padding(ip)
            .padding(horizontal = 4.dp),
    ) {
        stickyHeader(
            key = "current_title",
            contentType = HostedSyncGroup::class,
        ) {
            uiState.hostedSyncGroups.find { it.isDefault }?.let {
                HostedSyncGroupItem(
                    hostedSyncGroup = it,
                    execute = execute,
                )
            }
        }

        items(
            items = uiState.receivedPairRequests,
            key = { it.deviceId },
            contentType = { it::class },
        ) { node ->
            NodeItem(
                node = node,
                execute = execute,
            )
        }

        stickyHeader(
            key = "available_title",
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 12.dp),
            ) {
                Text(
                    text = Res.string.available_sync_groups.getString(),
                    fontSize = 20.sp,
                )
            }
        }
        items(
            items = uiState.hostedSyncGroups.filter { !it.isDefault },
            key = { it.hostedSyncGroupId },
            contentType = { it::class },
        ) {
            HostedSyncGroupItem(
                hostedSyncGroup = it,
                execute = execute,
            )
        }

        stickyHeader(
            key = "createGroup_title",
            contentType = "title"
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
                    onClick = showCreateGroupDialog,
                ) {
                    Icon(
                        painter = Res.drawable.plus.getPainter(),
                        contentDescription = "discover"
                    )
                }
            }
        }

    }
}
