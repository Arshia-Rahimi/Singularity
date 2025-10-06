package com.github.singularity.ui.feature.broadcast

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.github.singularity.ui.designsystem.components.DrawerIcon
import com.github.singularity.ui.designsystem.components.ScreenScaffold
import com.github.singularity.ui.designsystem.components.dialogs.ConfirmationDialog
import com.github.singularity.ui.designsystem.components.dialogs.InputDialog
import com.github.singularity.ui.feature.broadcast.components.HostedSyncGroupItem
import com.github.singularity.ui.feature.broadcast.components.NodeItem
import org.koin.compose.viewmodel.koinViewModel
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.broadcast
import singularity.composeapp.generated.resources.client
import singularity.composeapp.generated.resources.create
import singularity.composeapp.generated.resources.create_new_sync_group
import singularity.composeapp.generated.resources.hosted_sync_groups
import singularity.composeapp.generated.resources.pair_requests
import singularity.composeapp.generated.resources.plus
import singularity.composeapp.generated.resources.refresh
import singularity.composeapp.generated.resources.switch_to_client

@Composable
fun BroadcastScreen(
    openDrawer: () -> Unit,
) {
    val viewModel = koinViewModel<BroadcastViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BroadcastScreen(
        uiState = uiState,
        execute = {
            when (this) {
                is BroadcastIntent.OpenDrawer -> openDrawer()
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
    var showCreateGroupDialog by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    ScreenScaffold(
        topBar = {
            TopAppBar(
                title = { Text(Res.string.broadcast.getString()) },
                navigationIcon = {
                    DrawerIcon { BroadcastIntent.OpenDrawer.execute() }
                },
                actions = {
                    IconButton(
                        onClick = { showCreateGroupDialog = true },
                    ) {
                        Icon(
                            painter = Res.drawable.plus.getPainter(),
                            contentDescription = Res.string.create_new_sync_group.getString(),
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
        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .padding(ip)
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            stickyHeader {
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
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = Res.string.hosted_sync_groups.getString(),
                        fontSize = 20.sp,
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                    )
                }
            }

            items(
                items = uiState.hostedSyncGroups,
                key = { it.hostedSyncGroupId },
            ) {
                HostedSyncGroupItem(
                    hostedSyncGroup = it,
                    execute = execute,
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = Res.string.pair_requests.getString(),
                        fontSize = 20.sp,
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                    )
                }
            }

            items(
                items = uiState.receivedPairRequests,
                key = { it.deviceId },
            ) {
                NodeItem(it, execute)
            }
        }

    }

    InputDialog(
        visible = showCreateGroupDialog,
        onConfirm = { BroadcastIntent.CreateGroup(it).execute() },
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
        onConfirm = { BroadcastIntent.ToggleSyncMode.execute() },
        onDismiss = { showSwitchModeDialog = false },
        message = Res.string.switch_to_client.getString(),
    )
}
