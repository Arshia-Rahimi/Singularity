package com.github.singularity.ui.feature.connection.server.pages

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.singularity.core.shared.model.ServerConnectionState
import com.github.singularity.ui.designsystem.components.TopBar
import com.github.singularity.ui.designsystem.shared.getPainter
import com.github.singularity.ui.designsystem.shared.getString
import com.github.singularity.ui.feature.connection.server.ServerIntent
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.back
import singularity.composeapp.generated.resources.list

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SyncGroupDetails(
	connectionState: ServerConnectionState.Running,
	execute: ServerIntent.() -> Unit,
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
	        TopBar(
		        title = connectionState.group.name,
		        actions = {
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

    }
}
