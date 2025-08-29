package com.github.singularity.ui.feature.broadcast.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import com.github.singularity.core.shared.compose.getPainter
import com.github.singularity.core.shared.compose.getString
import com.github.singularity.ui.feature.broadcast.BroadcastIntent
import com.github.singularity.ui.feature.broadcast.BroadcastUiState
import org.jetbrains.compose.resources.painterResource
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.arrow_back
import singularity.composeapp.generated.resources.back
import singularity.composeapp.generated.resources.broadcast
import singularity.composeapp.generated.resources.create_new_sync_group
import singularity.composeapp.generated.resources.plus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BroadcastTopBar(
    uiState: BroadcastUiState,
    showCreateGroupDialog: () -> Unit,
    execute: BroadcastIntent.() -> Unit,
) {
    TopAppBar(
        title = { Text(Res.string.broadcast.getString()) },
        navigationIcon = {
            IconButton(
                onClick = { BroadcastIntent.NavBack.execute() },
                enabled = !uiState.isBroadcasting,
            ) {
                Icon(
                    painter = painterResource(Res.drawable.arrow_back),
                    contentDescription = Res.string.back.getString(),
                )
            }
        },
        actions = {
            IconButton(
                onClick = showCreateGroupDialog,
                enabled = !uiState.isBroadcasting,
            ) {
                Icon(
                    painter = Res.drawable.plus.getPainter(),
                    contentDescription = Res.string.create_new_sync_group.getString(),
                )
            }
        }
    )
}
