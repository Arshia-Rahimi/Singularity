package com.github.singularity.core.datasource.presence

import com.github.singularity.core.shared.model.HostedSyncGroup

interface DeviceBroadcastService {

    suspend fun startBroadcast(group: HostedSyncGroup)

    suspend fun stopBroadcast()

}
