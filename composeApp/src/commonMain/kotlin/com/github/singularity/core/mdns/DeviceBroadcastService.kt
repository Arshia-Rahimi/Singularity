package com.github.singularity.core.mdns

import com.github.singularity.core.database.entities.HostedSyncGroup

interface DeviceBroadcastService {

    suspend fun broadcastServer(group: HostedSyncGroup)

}
