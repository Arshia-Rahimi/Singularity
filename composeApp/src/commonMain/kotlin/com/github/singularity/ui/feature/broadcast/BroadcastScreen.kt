package com.github.singularity.ui.feature.broadcast

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.singularity.core.shared.compose.getString
import com.github.singularity.ui.designsystem.components.dialogs.InputDialog
import com.github.singularity.ui.feature.broadcast.components.BroadcastTopBar
import com.github.singularity.ui.feature.broadcast.components.HostedSyncGroupItem
import com.github.singularity.ui.feature.broadcast.components.NodeItem
import org.koin.compose.viewmodel.koinViewModel
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.create
import singularity.composeapp.generated.resources.create_new_sync_group
import singularity.composeapp.generated.resources.pair_requests

@Composable
fun BroadcastScreen(
    navBack: () -> Unit,
) {
    val viewModel = koinViewModel<BroadcastViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BroadcastScreen(
        uiState = uiState,
        execute = {
            when (this) {
                is BroadcastIntent.NavBack -> navBack()
                else -> viewModel.execute(this)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
private fun BroadcastScreen(
    uiState: BroadcastUiState,
    execute: BroadcastIntent.() -> Unit,
) {
    var showCreateGroupDialog by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            BroadcastTopBar(
                uiState = uiState,
                execute = execute,
                showCreateGroupDialog = { showCreateGroupDialog = true },
            )
        },
    ) { ip ->
        Column(
            modifier = Modifier.fillMaxSize().padding(ip),
        ) {
            if (uiState.isBroadcasting) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().weight(0.8f),
                ) {
                    item {
                        HostedSyncGroupItem(
                            hostedSyncGroup = uiState.syncGroups.first(),
                            execute = execute,
                        )

                        Text(
                            text = Res.string.pair_requests.getString(),
                            fontSize = 20.sp,
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                        )

                    }

                    items(
                        items = uiState.pairRequests,
                        key = { it.deviceId },
                    ) {
                        NodeItem(it, execute)
                    }
                }

            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().weight(0.8f),
                ) {
                    items(
                        items = uiState.syncGroups,
                        key = { it.hostedSyncGroupId },
                    ) {
                        HostedSyncGroupItem(
                            hostedSyncGroup = it,
                            execute = execute,
                        )
                    }
                }
            }

            Box(
                modifier = Modifier.fillMaxWidth().weight(0.2f),
                contentAlignment = Alignment.Center,
            ) {
                Button(
                    onClick = { if (uiState.isBroadcasting) BroadcastIntent.StopBroadcast.execute() else BroadcastIntent.Broadcast.execute() },
                ) {
                    Text(if (uiState.isBroadcasting) "stop broadcast" else "broadcast")
                }
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

}
