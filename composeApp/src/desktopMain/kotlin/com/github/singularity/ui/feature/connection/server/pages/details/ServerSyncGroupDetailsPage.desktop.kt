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
import com.github.singularity.ui.designsystem.components.Grid
import com.github.singularity.ui.designsystem.components.TopBar
import com.github.singularity.ui.designsystem.shared.getPainter
import com.github.singularity.ui.designsystem.shared.getString
import com.github.singularity.ui.feature.connection.server.Node
import com.github.singularity.ui.feature.connection.server.RunningSyncGroupServer
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
fun ServerSyncGroupDetailsPage(
    server: RunningSyncGroupServer,
    execute: ServerIntent.() -> Unit,
) {

	Scaffold(
		modifier = Modifier.fillMaxSize(),
		topBar = {
			TopBar(
                title = server.group.groupName,
				actions = {
					IconButton(
                        onClick = { ServerIntent.ToIndex.execute() }
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
		Grid(ip) {

            pairRequestItems(server.requestedNodes, execute)

            connectedDevicesItem(server.connectedNodes, execute)

			pairedAndNotConnectedItems(
                server.connectedNodes,
                server.pairedNodes,
				execute
			)

		}
	}
}

private fun LazyGridScope.pairRequestItems(
	requestedNodes: List<Node>,
	execute: ServerIntent.() -> Unit,
) {
	if (!requestedNodes.isEmpty()) {
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
			items = requestedNodes,
			key = { it.deviceId },
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
	connectedNodes: List<Node>,
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
				text = if (connectedNodes.isEmpty()) Res.string.no_connected_nodes.getString() else Res.string.connected_nodes.getString(),
				fontSize = 12.sp,
			)
		}
	}
	items(
		items = connectedNodes,
		key = { it.deviceId },
	) {
		NodeItem(
			node = it,
			execute = execute
		)
	}
}

private fun LazyGridScope.pairedAndNotConnectedItems(
	connectedNodes: List<Node>,
	pairedNodes: List<Node>,
	execute: ServerIntent.() -> Unit,
) {
	val pairedAndNotConnectedNodes = pairedNodes.filter { it !in connectedNodes }
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
					text = if (pairedNodes.isEmpty()) Res.string.no_paired_nodes.getString() else Res.string.paired_nodes.getString(),
					fontSize = 12.sp,
				)
			}
		}

		items(
			items = pairedAndNotConnectedNodes,
			key = { it.deviceId },
		) {
			NodeItem(
				node = it,
				execute = execute
			)
		}

	}
}
