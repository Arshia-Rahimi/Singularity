package com.github.singularity.core.data

import com.github.singularity.core.shared.model.HostedSyncGroup

interface ServerConnectionRepository {

    suspend fun startServer(group: HostedSyncGroup)

    suspend fun stopServer()

}
