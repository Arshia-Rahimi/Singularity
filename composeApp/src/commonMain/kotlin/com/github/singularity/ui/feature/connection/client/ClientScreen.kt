package com.github.singularity.ui.feature.connection.client

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.singularity.core.shared.compose.getPainter
import com.github.singularity.core.shared.compose.getString
import com.github.singularity.ui.designsystem.components.DrawerIcon
import com.github.singularity.ui.designsystem.components.NoRippleTextButton
import com.github.singularity.ui.designsystem.components.PulseAnimation
import com.github.singularity.ui.feature.connection.client.components.JoinedSyncGroupItem
import com.github.singularity.ui.feature.connection.client.components.ServerItem
import org.koin.compose.viewmodel.koinViewModel
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.available_servers
import singularity.composeapp.generated.resources.discover
import singularity.composeapp.generated.resources.joined_sync_groups
import singularity.composeapp.generated.resources.plus
import singularity.composeapp.generated.resources.searching_dotted
import singularity.composeapp.generated.resources.stop

@Composable
fun ClientScreen() {
    val viewModel = koinViewModel<ClientViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ClientScreen(
        uiState = uiState,
        execute = { viewModel.execute(this) },
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ClientScreen(
    uiState: ClientUiState,
    execute: ClientIntent.() -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(Res.string.discover.getString()) },
                navigationIcon = { DrawerIcon() },
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
                    modifier = Modifier.fillMaxWidth()
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

            stickyHeader(
                key = "available_title",
                contentType = "title",
            ) {
                AnimatedContent(uiState.availableServers) {
                    if (uiState.isDiscovering) {
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
                    } else {
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
                                    contentDescription = "discover"
                                )
                            }
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

            stickyHeader(
                key = "pulse_animation",
                contentType = "animation",
            ) {
                if (uiState.isDiscovering) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
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

                        NoRippleTextButton(
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

    }
}
