package com.github.singularity.ui.feature.connection.server.pages.details

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.singularity.core.shared.model.ServerConnectionState
import com.github.singularity.ui.designsystem.components.Grid
import com.github.singularity.ui.designsystem.components.TopBar
import com.github.singularity.ui.designsystem.shared.getPainter
import com.github.singularity.ui.designsystem.shared.getString
import com.github.singularity.ui.feature.connection.server.ServerIntent
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.back
import singularity.composeapp.generated.resources.connected_nodes
import singularity.composeapp.generated.resources.list
import singularity.composeapp.generated.resources.no_connected_nodes
import singularity.composeapp.generated.resources.no_paired_nodes
import singularity.composeapp.generated.resources.pair_requests
import singularity.composeapp.generated.resources.paired_nodes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HostedSyncGroupDetailsPage(
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
		Grid(ip) {

			pairRequestItems(connectionState, execute)

			connectedDevicesItem(connectionState, execute)

			pairedAndNotConnectedItems(connectionState, execute)

		}
	}
}

private fun LazyGridScope.pairRequestItems(
	connectionState: ServerConnectionState.Running,
	execute: ServerIntent.() -> Unit,
) {
	if (!connectionState.pairRequests.isEmpty()) {
		stickyHeader(
			key = "pair_requests_title",
		) {
			Row(
				modifier = Modifier
					.animateItem()
					.fillMaxWidth()
					.padding(vertical = 8.dp, horizontal = 12.dp),
			) {
				Text(
					text = Res.string.pair_requests.getString(),
					fontSize = 12.sp,
				)
			}
		}
		items(
			items = connectionState.pairRequests,
			key = { "pairRequest_${it.deviceId}" },
		) {
			NodeItem(
				node = it,
				execute = execute,
				isPairRequest = true,
			)
		}
	}
}

private fun LazyGridScope.connectedDevicesItem(
	connectionState: ServerConnectionState.Running,
	execute: ServerIntent.() -> Unit,
) {
	stickyHeader(
		key = "connected_title",
	) {
		Row(
			modifier = Modifier
				.animateItem()
				.fillMaxWidth()
				.padding(vertical = 8.dp, horizontal = 12.dp),
		) {
			Text(
				text = if (connectionState.connectedNodes.isEmpty()) Res.string.no_connected_nodes.getString() else Res.string.connected_nodes.getString(),
				fontSize = 12.sp,
			)
		}
	}
	items(
		items = connectionState.connectedNodes,
		key = { "connected_${it.deviceId}" },
	) {
		NodeItem(
			node = it.toNode(),
			execute = execute
		)
	}
}

private fun LazyGridScope.pairedAndNotConnectedItems(
	connectionState: ServerConnectionState.Running,
	execute: ServerIntent.() -> Unit,
) {
	val pairedAndNotConnectedNodes =
		connectionState.group.nodes.filter { it !in connectionState.connectedNodes }
	if (!pairedAndNotConnectedNodes.isEmpty()) {
		stickyHeader(
			key = "not_connected_title",
		) {
			Row(
				modifier = Modifier
					.animateItem()
					.fillMaxWidth()
					.padding(vertical = 8.dp, horizontal = 12.dp),
			) {
				Text(
					text = if (connectionState.group.nodes.isEmpty()) Res.string.no_paired_nodes.getString() else Res.string.paired_nodes.getString(),
					fontSize = 12.sp,
				)
			}
		}

		items(
			items = pairedAndNotConnectedNodes,
			key = { "pairedAndNotConnected_${it.deviceId}" },
		) {
			NodeItem(
				node = it.toNode(),
				execute = execute
			)
		}

	}
}
