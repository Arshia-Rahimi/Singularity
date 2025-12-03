package com.github.singularity.core.datasource.network

import com.github.singularity.core.datasource.database.JoinedSyncGroupModel
import kotlinx.coroutines.flow.Flow

interface DeviceDiscoveryService {

	fun discoverServers(): Flow<List<LocalServerModel>>

	suspend fun findServer(syncGroup: JoinedSyncGroupModel): LocalServerModel?

}