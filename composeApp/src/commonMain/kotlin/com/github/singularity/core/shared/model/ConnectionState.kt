package com.github.singularity.core.shared.model

import com.github.singularity.core.database.entities.JoinedSyncGroup
import com.github.singularity.models.IServer

sealed interface ConnectionState {
    data object NoDefaultServer : ConnectionState
    data class Searching(val joinedSyncGroup: JoinedSyncGroup) : ConnectionState
    data class Connected(val server: IServer) : ConnectionState
    data class ServerNotFound(val joinedSyncGroup: JoinedSyncGroup, val message: String) :
        ConnectionState
}
