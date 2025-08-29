package com.github.singularity.ui.feature.broadcast

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.singularity.core.shared.compose.getPainter
import com.github.singularity.core.shared.compose.getString
import com.github.singularity.ui.designsystem.components.dialogs.InputDialog
import com.github.singularity.ui.feature.broadcast.components.HostedSyncGroupItem
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.arrow_back
import singularity.composeapp.generated.resources.back
import singularity.composeapp.generated.resources.broadcast
import singularity.composeapp.generated.resources.create
import singularity.composeapp.generated.resources.create_new_sync_group
import singularity.composeapp.generated.resources.plus

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

@OptIn(ExperimentalMaterial3Api::class)
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
            TopAppBar(
                title = { Text(Res.string.broadcast.getString()) },
                navigationIcon = {
                    IconButton(
                        onClick = { BroadcastIntent.NavBack.execute() },
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.arrow_back),
                            contentDescription = Res.string.back.getString(),
                        )
                    }
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
                }
            )
        },
    ) { ip ->
        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .padding(ip)
                .padding(4.dp),
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
