package com.github.singularity.ui.feature.main.components.broadcast

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.singularity.core.shared.compose.getString
import com.github.singularity.ui.feature.main.BroadcastUiState
import com.github.singularity.ui.feature.main.MainIntent
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.pair_requests

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun ColumnScope.BroadcastSection(
    uiState: BroadcastUiState,
    execute: MainIntent.BroadcastIntent.() -> Unit,
) {

    if (uiState.isBroadcasting) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().weight(0.8f),
        ) {
            item {
                HostedSyncGroupItem(
                    hostedSyncGroup = uiState.syncGroups.first(),
                    execute = execute,
                    optionsEnabled = false,
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
                    optionsEnabled = !uiState.isBroadcasting,
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
            enabled = uiState.syncGroups.any { it.isDefault },
        ) {
            Text(if (uiState.isBroadcasting) "stop broadcast" else "broadcast")
        }
    }

}
