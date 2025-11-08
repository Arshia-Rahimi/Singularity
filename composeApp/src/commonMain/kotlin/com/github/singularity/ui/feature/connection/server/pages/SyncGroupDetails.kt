package com.github.singularity.ui.feature.connection.server.pages

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.github.singularity.core.shared.compose.getPainter
import com.github.singularity.core.shared.compose.getString
import com.github.singularity.ui.feature.connection.server.ServerIntent
import com.github.singularity.ui.feature.connection.server.ServerUiState
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.back
import singularity.composeapp.generated.resources.list
import singularity.composeapp.generated.resources.select_hosted_sync_groups
import singularity.composeapp.generated.resources.server
import singularity.composeapp.generated.resources.switch_to_server

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SyncGroupDetails(
    uiState: ServerUiState,
    execute: ServerIntent.() -> Unit,
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = Res.string.select_hosted_sync_groups.getString(),
                        fontSize = 16.sp,
                    )
                },
                actions = {
                    IconButton(
                        onClick = { },
                    ) {
                        Icon(
                            painter = Res.drawable.server.getPainter(),
                            contentDescription = Res.string.switch_to_server.getString(),
                        )
                    }
                    IconButton(
                        onClick = { ServerIntent.RemoveAllDefaults.execute() }
                    ) {
                        Icon(
                            painter = Res.drawable.list.getPainter(),
                            contentDescription = Res.string.back.getString(),
                        )
                    }
                }
            )
        },
    ) { ip ->
        Text(uiState.hostedSyncGroups.first { it.isDefault }.name)
    }
}
