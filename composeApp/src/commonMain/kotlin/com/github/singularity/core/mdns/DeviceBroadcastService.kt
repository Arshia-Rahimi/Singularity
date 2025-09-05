package com.github.singularity.core.mdns

import com.github.singularity.core.shared.model.HostedSyncGroup

interface DeviceBroadcastService {

    suspend fun broadcastServer(group: HostedSyncGroup)

}
