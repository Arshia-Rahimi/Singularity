package com.github.singularity.core.shared.model

import androidx.compose.runtime.Composable
import com.github.singularity.ui.designsystem.shared.getString
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.connection_failed
import singularity.composeapp.generated.resources.no_default_server
import singularity.composeapp.generated.resources.searching
import singularity.composeapp.generated.resources.server_running

sealed interface SyncState {
	val message: String
		@Composable get
}

sealed interface ClientSyncState : SyncState {

	data object Loading : ClientSyncState {
		override val message: String
			@Composable get() = ""
	}

	data object NoDefaultServer : ClientSyncState {
		override val message: String
			@Composable get() = Res.string.no_default_server.getString()
	}

	sealed interface WithDefaultServer : ClientSyncState {
		val joinedSyncGroupName: String
	}

	data class Searching(
		override val joinedSyncGroupName: String,
	) : WithDefaultServer {
		override val message: String
			@Composable get() = Res.string.searching.getString(joinedSyncGroupName)
	}

	data class SyncFailed(
		override val joinedSyncGroupName: String,
		val server: LocalServer,
	) : WithDefaultServer {
		override val message: String
			@Composable get() = Res.string.connection_failed.getString(joinedSyncGroupName)
	}

	data class Connected(
		override val joinedSyncGroupName: String,
		val server: LocalServer,
	) : WithDefaultServer {
		override val message: String
			@Composable get() = Res.string.connection_failed.getString(joinedSyncGroupName)
	}

	data class ServerNotFound(
		override val joinedSyncGroupName: String,
		val errorMessage: String,
	) : WithDefaultServer {
		override val message: String
			@Composable get() = Res.string.connection_failed.getString(joinedSyncGroupName)
	}

}

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
		val group: HostedSyncGroup,
		val connectedNodes: List<HostedSyncGroupNode>,
		val pairRequests: List<Node>,
	) : ServerSyncState {
		override val message: String
			@Composable get() = Res.string.server_running.getString(group.name, connectedNodes.size)
	}

}
