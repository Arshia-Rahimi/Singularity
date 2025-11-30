package com.github.singularity.ui.feature.connection.client.pages.details

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.singularity.core.shared.model.ClientSyncState
import com.github.singularity.ui.designsystem.components.TopBar
import com.github.singularity.ui.designsystem.shared.getPainter
import com.github.singularity.ui.designsystem.shared.getString
import com.github.singularity.ui.feature.connection.client.ClientIntent
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.back
import singularity.composeapp.generated.resources.list
import singularity.composeapp.generated.resources.refresh

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinedSyncGroupDetailsPage(
	syncState: ClientSyncState.WithDefaultServer,
	execute: ClientIntent.() -> Unit,
) {
	Scaffold(
		topBar = {
			TopBar(
				title = syncState.joinedSyncGroupName,
				actions = {
					IconButton(
						onClick = { ClientIntent.ToIndex.execute() }
					) {
						Icon(
							painter = Res.drawable.list.getPainter(),
							contentDescription = Res.string.back.getString(),
						)
					}
				},
			)
		},
	) { ip ->
		Column(
			modifier = Modifier
				.padding(ip)
				.fillMaxSize()
		) {
			Row(
				modifier = Modifier.fillMaxWidth()
					.padding(bottom = 4.dp)
					.padding(horizontal = 4.dp),
				horizontalArrangement = Arrangement.SpaceBetween,
			) {
				AnimatedContent(syncState) {
					Text(it.message)
				}

				IconButton(
					onClick = { ClientIntent.RefreshConnection.execute() },
				) {
					Icon(
						painter = Res.drawable.refresh.getPainter(),
						contentDescription = Res.string.refresh.getString()
					)
				}
			}
		}
	}
}
