package com.github.singularity.core.mdns

import com.github.singularity.data.entities.HostedSyncGroup

interface DeviceBroadcastService {

    suspend fun broadcastServer(group: HostedSyncGroup)

}
