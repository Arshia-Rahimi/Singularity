package com.github.singularity.ui.feature.home.client.pages.discover

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.singularity.core.shared.AppTheme
import com.github.singularity.core.shared.compose.getPainter
import com.github.singularity.core.shared.compose.getString
import com.github.singularity.core.shared.model.JoinedSyncGroup
import com.github.singularity.ui.designsystem.theme.SingularityTheme
import com.github.singularity.ui.feature.home.client.ClientIntent
import com.github.singularity.ui.feature.home.client.ClientUiState
import com.github.singularity.ui.feature.home.client.pages.discover.components.JoinedSyncGroupItem
import org.jetbrains.compose.ui.tooling.preview.Preview
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.available_servers
import singularity.composeapp.generated.resources.joined_sync_groups
import singularity.composeapp.generated.resources.plus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverPage(
    uiState: ClientUiState,
    execute: ClientIntent.() -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(200.dp),
        modifier = Modifier.fillMaxSize()
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
            items = (1..9).map {
                JoinedSyncGroup(
                    authToken = "",
                    isDefault = false,
                    syncGroupId = it.toString(),
                    syncGroupName = (1..8).map { ('a'..'z').random() }.joinToString("")
                )
            },
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
                if (it != null) {
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
    }
}

@Preview
@Composable
private fun Preview() {
    SingularityTheme(AppTheme.Light) {
        DiscoverPage(
            execute = {},
            uiState = ClientUiState(
                joinedSyncGroups = (1..9).map {
                    JoinedSyncGroup(
                        authToken = "",
                        isDefault = false,
                        syncGroupId = it.toString(),
                        syncGroupName = (1..8).map { ('a'..'z').random() }.joinToString("")
                    )
                }.toMutableStateList(),
            ),
        )
    }
}
