package com.github.singularity.core.syncservice

import androidx.compose.runtime.Composable
import com.github.singularity.core.datasource.database.HostedSyncGroupModel
import com.github.singularity.core.datasource.database.HostedSyncGroupNodeModel
import com.github.singularity.core.datasource.network.NodeDto
import com.github.singularity.ui.designsystem.shared.getString
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.no_default_server
import singularity.composeapp.generated.resources.server_running

sealed interface ServerSyncState : SyncState {

	data object Loading : ServerSyncState {
		override val message: String
			@Composable get() = ""
	}

	data object NoDefaultServer : ServerSyncState {
		override val message: String
			@Composable get() = Res.string.no_default_server.getString()
	}

	data class Running(
		val group: HostedSyncGroupModel,
		val connectedNodes: List<HostedSyncGroupNodeModel>,
		val pairRequests: List<NodeDto>,
	) : ServerSyncState {
		override val message: String
			@Composable get() = Res.string.server_running.getString(group.name, connectedNodes.size)
	}

}
