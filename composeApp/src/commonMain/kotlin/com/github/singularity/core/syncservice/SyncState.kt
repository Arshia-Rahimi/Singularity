package com.github.singularity.core.syncservice

import androidx.compose.runtime.Composable
import com.github.singularity.ui.designsystem.shared.getString
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.connected
import singularity.composeapp.generated.resources.connection_failed
import singularity.composeapp.generated.resources.no_default_server
import singularity.composeapp.generated.resources.searching
import singularity.composeapp.generated.resources.server_not_found

interface SyncState {
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

	data class ConnectionFailed(
		override val joinedSyncGroupName: String,
	) : WithDefaultServer {
		override val message: String
			@Composable get() = Res.string.connection_failed.getString(joinedSyncGroupName)
	}

	data class Connected(
		override val joinedSyncGroupName: String,
	) : WithDefaultServer {
		override val message: String
			@Composable get() = Res.string.connected.getString(joinedSyncGroupName)
	}

	data class ServerNotFound(
		override val joinedSyncGroupName: String,
		val errorMessage: String,
	) : WithDefaultServer {
		override val message: String
			@Composable get() = Res.string.server_not_found.getString(joinedSyncGroupName)
	}

}
