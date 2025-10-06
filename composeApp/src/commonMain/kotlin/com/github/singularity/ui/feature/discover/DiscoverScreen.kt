package com.github.singularity.ui.feature.discover

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.singularity.core.shared.canHostSyncServer
import com.github.singularity.core.shared.compose.getPainter
import com.github.singularity.core.shared.compose.getString
import com.github.singularity.ui.designsystem.components.DrawerIcon
import com.github.singularity.ui.designsystem.components.ScreenScaffold
import com.github.singularity.ui.designsystem.components.dialogs.ConfirmationDialog
import com.github.singularity.ui.feature.discover.components.JoinedSyncGroupItem
import com.github.singularity.ui.feature.discover.components.PairRequestState
import com.github.singularity.ui.feature.discover.components.ServerItem
import org.koin.compose.viewmodel.koinViewModel
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.approved_to_join
import singularity.composeapp.generated.resources.available_servers
import singularity.composeapp.generated.resources.await_pair_request_approval
import singularity.composeapp.generated.resources.discover
import singularity.composeapp.generated.resources.paired_servers
import singularity.composeapp.generated.resources.refresh
import singularity.composeapp.generated.resources.server
import singularity.composeapp.generated.resources.switch_to_server

@Composable
fun DiscoverScreen(
    openDrawer: () -> Unit,
) {
    val viewModel = koinViewModel<DiscoverViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DiscoverScreen(
        uiState = uiState,
        execute = {
            when (this) {
                is DiscoverIntent.OpenDrawer -> openDrawer()
                else -> viewModel.execute(this)
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiscoverScreen(
    uiState: DiscoverUiState,
    execute: DiscoverIntent.() -> Unit,
) {
    var showSwitchModeDialog by remember { mutableStateOf(false) }

    ScreenScaffold(
        topBar = {
            TopAppBar(
                title = { Text(Res.string.discover.getString()) },
                navigationIcon = {
                    DrawerIcon { DiscoverIntent.OpenDrawer.execute() }
                },
            )
        },
        floatingActionButton = {
            if (canHostSyncServer) {
                FloatingActionButton(
                    onClick = { showSwitchModeDialog = true },
                ) {
                    Icon(
                        painter = Res.drawable.server.getPainter(),
                        contentDescription = Res.string.switch_to_server.getString()
                    )
                }
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
                        onClick = { DiscoverIntent.RefreshConnection.execute() },
                    ) {
                        Icon(
                            painter = Res.drawable.refresh.getPainter(),
                            contentDescription = Res.string.refresh.getString(),
                        )
                    }
                }

                AnimatedContent(
                    targetState = uiState.sentPairRequestState,
                ) {
                    if (it !is PairRequestState.Idle) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .padding(vertical = 16.dp),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            when (uiState.sentPairRequestState) {
                                is PairRequestState.Awaiting -> {
                                    Text(Res.string.await_pair_request_approval.getString(uiState.sentPairRequestState.server.syncGroupName))
                                    CircularProgressIndicator()
                                }

                                is PairRequestState.Success -> Text(
                                    Res.string.approved_to_join.getString(
                                        uiState.sentPairRequestState.server
                                    )
                                )

                                is PairRequestState.Error -> Text(uiState.sentPairRequestState.message)
                                is PairRequestState.Idle -> Unit
                            }
                        }
                    }
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(vertical = 8.dp),
                ) {
                    Text(
                        text = Res.string.paired_servers.getString(),
                        fontSize = 16.sp,
                    )
                }
            }
            items(uiState.joinedSyncGroups) {
                JoinedSyncGroupItem(
                    joinedSyncGroup = it,
                    execute = execute,
                )
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(vertical = 8.dp),
                ) {
                    Text(
                        text = Res.string.available_servers.getString(),
                        fontSize = 16.sp,
                    )
                }
            }
            items(uiState.availableServers) {
                ServerItem(
                    server = it,
                    execute = execute,
                )
            }
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    Button(
                        onClick = { DiscoverIntent.RefreshDiscovery.execute() },
                    ) {
                        Text(Res.string.refresh.getString())
                    }
                }
            }
        }

        ConfirmationDialog(
            visible = showSwitchModeDialog && canHostSyncServer,
            onConfirm = { DiscoverIntent.ToggleSyncMode.execute() },
            onDismiss = { showSwitchModeDialog = false },
            message = Res.string.switch_to_server.getString(),
        )

    }
}
