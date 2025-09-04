package com.github.singularity.core.shared.model

import com.github.singularity.core.database.entities.JoinedSyncGroup
import org.jetbrains.compose.resources.StringResource
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.connected
import singularity.composeapp.generated.resources.connection_failed
import singularity.composeapp.generated.resources.no_default_server
import singularity.composeapp.generated.resources.searching
import singularity.composeapp.generated.resources.server_not_found

sealed interface ConnectionState {

    val message: StringResource

    data object NoDefaultServer : ConnectionState {
        override val message = Res.string.no_default_server
    }

    data class Searching(val joinedSyncGroup: JoinedSyncGroup) : ConnectionState {
        override val message = Res.string.searching
    }

    data class ConnectionFailed(val server: LocalServer, val errorMessage: String) :
        ConnectionState {
        override val message = Res.string.connection_failed
    }

    data class Connected(val server: LocalServer) : ConnectionState {
        override val message = Res.string.connected
    }

    data class ServerNotFound(
        val joinedSyncGroup: JoinedSyncGroup,
        val errorMessage: String,
    ) : ConnectionState {
        override val message = Res.string.server_not_found
    }

}
