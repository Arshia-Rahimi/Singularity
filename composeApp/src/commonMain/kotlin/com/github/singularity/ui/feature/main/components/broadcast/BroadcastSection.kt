package com.github.singularity.ui.feature.main.components.broadcast

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.singularity.core.shared.compose.getPainter
import com.github.singularity.core.shared.compose.getString
import com.github.singularity.ui.designsystem.components.dialogs.InputDialog
import com.github.singularity.ui.feature.main.BroadcastUiState
import com.github.singularity.ui.feature.main.MainIntent
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.arrow_back
import singularity.composeapp.generated.resources.back
import singularity.composeapp.generated.resources.create
import singularity.composeapp.generated.resources.create_new_sync_group
import singularity.composeapp.generated.resources.pair_requests
import singularity.composeapp.generated.resources.plus

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun ColumnScope.BroadcastSection(
    uiState: BroadcastUiState,
    execute: MainIntent.BroadcastIntent.() -> Unit,
) {
    var showHostedSyncGroupsDialog by remember { mutableStateOf(false) }
    var showCreateGroupDialog by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    LazyColumn(
        modifier = Modifier.fillMaxSize()
            .weight(1f),
    ) {
        item {
            HostedSyncGroupItem(
                hostedSyncGroup = uiState.hostedSyncGroups.first(),
                execute = execute,
            )

            Text(
                text = Res.string.pair_requests.getString(),
                fontSize = 20.sp,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
            )

        }

        items(
            items = uiState.receivedPairRequests,
            key = { it.deviceId },
        ) {
            NodeItem(it, execute)
        }
    }

    if (showHostedSyncGroupsDialog) {
        BasicAlertDialog(
            onDismissRequest = { showHostedSyncGroupsDialog = false },
            modifier = Modifier.padding(vertical = 24.dp, horizontal = 12.dp)
                .fillMaxSize(),
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        IconButton(
                            onClick = { showHostedSyncGroupsDialog = false },
                        ) {
                            Icon(
                                painter = Res.drawable.arrow_back.getPainter(),
                                contentDescription = Res.string.back.getString(),
                            )
                        }
                        IconButton(
                            onClick = { showCreateGroupDialog = true },
                        ) {
                            Icon(
                                painter = Res.drawable.plus.getPainter(),
                                contentDescription = Res.string.create_new_sync_group.getString(),
                            )
                        }
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
            }

            InputDialog(
                visible = showCreateGroupDialog,
                onConfirm = { MainIntent.BroadcastIntent.CreateGroup(it).execute() },
                onDismiss = {
                    showCreateGroupDialog = false
                    focusManager.clearFocus()
                },
                confirmText = Res.string.create.getString(),
                title = Res.string.create_new_sync_group.getString(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            )
        }
    }

}
