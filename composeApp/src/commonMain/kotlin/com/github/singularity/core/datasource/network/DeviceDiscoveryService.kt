package com.github.singularity.core.datasource.network

import com.github.singularity.core.datasource.database.JoinedSyncGroupModel
import kotlinx.coroutines.flow.Flow

interface DeviceDiscoveryService {

	fun discoverServers(): Flow<List<LocalServerDto>>

	suspend fun findServer(syncGroup: JoinedSyncGroupModel): LocalServerDto?

}