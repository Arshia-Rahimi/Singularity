package com.github.singularity.core.mdns

import com.github.singularity.core.shared.model.SyncGroup

interface DeviceBroadcastService {

    suspend fun broadcastServer(group: SyncGroup)

}
