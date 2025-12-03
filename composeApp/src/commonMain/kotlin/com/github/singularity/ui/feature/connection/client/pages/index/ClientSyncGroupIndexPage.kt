package com.github.singularity.ui.feature.connection.client.pages.index

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.singularity.ui.designsystem.components.Grid
import com.github.singularity.ui.designsystem.components.TopBar
import com.github.singularity.ui.designsystem.shared.getString
import com.github.singularity.ui.feature.connection.client.ClientIntent
import com.github.singularity.ui.feature.connection.client.ClientUiState
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.available_servers
import singularity.composeapp.generated.resources.await_pair_request_approval
import singularity.composeapp.generated.resources.discover
import singularity.composeapp.generated.resources.joined_sync_groups
import singularity.composeapp.generated.resources.rejected_pair_request_approval

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientSyncGroupIndexPage(
	uiState: ClientUiState,
	execute: ClientIntent.() -> Unit,
) {
	Scaffold(
		topBar = {
			TopBar(
				title = Res.string.discover.getString(),
			)
		},
	) { ip ->
		Grid(ip) {
			joinedSyncGroupItems(uiState, execute)
			sentPairRequestItem(uiState)
			discoveredServerItems(uiState, execute)
		}
	}
}

private fun LazyGridScope.sentPairRequestItem(
	uiState: ClientUiState,
) {
	val pairRequestState = uiState.sentPairRequestState
	if (pairRequestState != PairRequestState.Idle) {
		stickyHeader(
			key = "sent_pair_request_title",
		) {
			when (pairRequestState) {
				is PairRequestState.Awaiting -> Text(
					Res.string.await_pair_request_approval.getString(
						pairRequestState.server.groupName
					)
				)

				is PairRequestState.Error -> Text(
					Res.string.rejected_pair_request_approval.getString(
						pairRequestState.message
					)
				)

				else -> Unit
			}
		}
	}
}

private fun LazyGridScope.joinedSyncGroupItems(
	uiState: ClientUiState,
	execute: ClientIntent.() -> Unit,
) {
	if (!uiState.joinedSyncGroups.isEmpty()) {
		stickyHeader(
			key = "joined_title",
			contentType = "title",
		) {
			Row(
				modifier = Modifier
					.animateItem()
					.fillMaxWidth()
					.padding(vertical = 8.dp, horizontal = 12.dp),
			) {
				Text(
					text = Res.string.joined_sync_groups.getString(),
					fontSize = 20.sp,
				)
			}
		}

		items(
			items = uiState.joinedSyncGroups.distinctBy { it.groupId },
			key = { "joined_${it.groupId}" },
			contentType = { it },
		) {
			JoinedSyncGroupItem(
				joinedSyncGroup = it,
				execute = execute,
			)
		}
	}
}

private fun LazyGridScope.discoveredServerItems(
	uiState: ClientUiState,
	execute: ClientIntent.() -> Unit,
) {
	if (!uiState.discoveredServers.isEmpty()) {
		stickyHeader(
			key = "available_title",
			contentType = "title",
		) {
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
		}

		items(
			items = uiState.discoveredServers.distinctBy { it.groupId },
			key = { "available${it.groupId}" },
			contentType = { it },
		) {
			ServerItem(
				server = it,
				execute = execute,
			)
		}
	}
}
