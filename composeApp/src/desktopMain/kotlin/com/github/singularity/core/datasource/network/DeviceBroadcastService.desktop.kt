package com.github.singularity.core.datasource.network

import com.github.singularity.core.datasource.database.HostedSyncGroupModel

interface DeviceBroadcastService {

	suspend fun startBroadcast(group: HostedSyncGroupModel)

    suspend fun stopBroadcast()

}
