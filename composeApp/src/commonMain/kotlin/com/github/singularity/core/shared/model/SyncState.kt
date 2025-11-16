package com.github.singularity.core.shared.model

import androidx.compose.runtime.Composable
import com.github.singularity.ui.designsystem.shared.getString
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.connected
import singularity.composeapp.generated.resources.connection_failed
import singularity.composeapp.generated.resources.no_default_server
import singularity.composeapp.generated.resources.searching
import singularity.composeapp.generated.resources.server_not_found
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

    class WithDefaultServer(
        val joinedSyncGroup: JoinedSyncGroup,
        val connectionState: ClientConnectionState,
    ) : ClientSyncState {
        override val message: String
            @Composable get() = connectionState.message(joinedSyncGroup.syncGroupName)
    }

}

sealed interface ClientConnectionState {

    @Composable
    fun message(name: String): String

    data object Searching : ClientConnectionState {
        @Composable
        override fun message(name: String) =
            Res.string.searching.getString(name)
    }

    data class SyncFailed(
        val server: LocalServer,
    ) : ClientConnectionState {
        @Composable
        override fun message(name: String) =
            Res.string.connection_failed.getString(name)
    }

    data class Connected(
        val server: LocalServer,
    ) : ClientConnectionState {
        @Composable
        override fun message(name: String) = Res.string.connected.getString(name)
    }

    data class ServerNotFound(
        val errorMessage: String,
    ) : ClientConnectionState {
        @Composable
        override fun message(name: String) =
            Res.string.server_not_found.getString(name)
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
