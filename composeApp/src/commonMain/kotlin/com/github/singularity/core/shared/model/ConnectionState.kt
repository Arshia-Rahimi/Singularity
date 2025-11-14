package com.github.singularity.core.shared.model

import androidx.compose.runtime.Composable
import com.github.singularity.ui.designsystem.shared.getString
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.connected
import singularity.composeapp.generated.resources.connection_dropped
import singularity.composeapp.generated.resources.connection_failed
import singularity.composeapp.generated.resources.no_default_server
import singularity.composeapp.generated.resources.searching
import singularity.composeapp.generated.resources.server_not_found
import singularity.composeapp.generated.resources.server_running

sealed interface ConnectionState {
    val message: String
        @Composable get
}

sealed interface ClientConnectionState : ConnectionState {

    data object NoDefaultServer : ClientConnectionState {
        override val message: String
            @Composable get() = Res.string.no_default_server.getString()
    }

    data class Searching(val joinedSyncGroup: JoinedSyncGroup) : ClientConnectionState {
        override val message: String
            @Composable get() = Res.string.searching.getString(joinedSyncGroup.syncGroupName)
    }

    data class ConnectionFailed(val server: LocalServer) :
        ClientConnectionState {
        override val message: String
            @Composable get() = Res.string.connection_failed.getString(server.syncGroupName)
    }

    data class ConnectionDropped(val server: LocalServer) :
        ClientConnectionState {
        override val message: String
            @Composable get() = Res.string.connection_dropped.getString(server.syncGroupName)
    }

    data class Connected(val server: LocalServer) : ClientConnectionState {
        override val message: String
            @Composable get() = Res.string.connected.getString(server.syncGroupName)
    }

    data class ServerNotFound(
        val joinedSyncGroup: JoinedSyncGroup,
        val errorMessage: String,
    ) : ClientConnectionState {
        override val message: String
            @Composable get() = Res.string.server_not_found.getString(joinedSyncGroup.syncGroupName)
    }

}

sealed interface ServerConnectionState : ConnectionState {

    data object NoDefaultServer : ServerConnectionState {
        override val message: String
            @Composable get() = Res.string.no_default_server.getString()
    }

    data class Running(
        val group: HostedSyncGroup,
        val connectedNodes: List<HostedSyncGroupNode>,
        val pairRequests: List<Node>,
    ) : ServerConnectionState {
        override val message: String
	        @Composable get() = Res.string.server_running.getString(group.name, connectedNodes.size)
    }

}
