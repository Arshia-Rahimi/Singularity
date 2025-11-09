package com.github.singularity.ui.feature.connection.server.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.github.singularity.core.shared.compose.getPainter
import com.github.singularity.core.shared.compose.getString
import com.github.singularity.ui.designsystem.components.TopBar
import com.github.singularity.ui.designsystem.components.dialogs.ConfirmationDialog
import com.github.singularity.ui.designsystem.components.dialogs.InputDialog
import com.github.singularity.ui.feature.connection.server.ServerIntent
import com.github.singularity.ui.feature.connection.server.ServerUiState
import com.github.singularity.ui.feature.connection.server.components.HostedSyncGroupItem
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.available_sync_groups
import singularity.composeapp.generated.resources.client
import singularity.composeapp.generated.resources.create
import singularity.composeapp.generated.resources.create_new_sync_group
import singularity.composeapp.generated.resources.plus
import singularity.composeapp.generated.resources.select_hosted_sync_groups
import singularity.composeapp.generated.resources.switch_to_client

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SyncGroupIndex(
    uiState: ServerUiState,
    execute: ServerIntent.() -> Unit,
) {
    var showSwitchModeDialog by remember { mutableStateOf(false) }
    var showCreateGroupDialog by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
	        TopBar(
		        title = Res.string.select_hosted_sync_groups.getString(),
		        actions = {
			        IconButton(
				        onClick = { showSwitchModeDialog = true },
			        ) {
				        Icon(
					        painter = Res.drawable.client.getPainter(),
					        contentDescription = Res.string.switch_to_client.getString(),
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
                key = "available_title",
            ) {
                Row(
                    modifier = Modifier
                        .animateItem()
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 12.dp),
                ) {
                    Text(
                        text = Res.string.available_sync_groups.getString(),
                        fontSize = 20.sp,
                    )
                }
            }

            items(
                items = uiState.hostedSyncGroups,
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
                        .padding(vertical = 8.dp)
                        .padding(bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    IconButton(
                        onClick = { showCreateGroupDialog = true },
                    ) {
                        Icon(
                            painter = Res.drawable.plus.getPainter(),
                            contentDescription = "discover"
                        )
                    }
                }
            }

        }

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
            visible = showSwitchModeDialog,
            message = Res.string.switch_to_client.getString(),
            onDismiss = { showSwitchModeDialog = false },
            onConfirm = { ServerIntent.ToggleSyncMode.execute() },
        )
    }
}
