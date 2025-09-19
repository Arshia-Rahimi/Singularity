package com.github.singularity.core.data

import com.github.singularity.core.shared.model.ServerConnectionState
import kotlinx.coroutines.flow.Flow

interface ServerConnectionRepository {

    fun runServer(): Flow<ServerConnectionState>

}
